    package com.projects.RecruiterManagement.service;

    import java.io.IOException;
    import java.io.InputStream;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.time.Duration;

    import com.projects.RecruiterManagement.storage.StorageException;
    import com.projects.RecruiterManagement.config.StorageProperties;
    import org.springframework.beans.factory.annotation.Autowired;

    import org.springframework.core.io.FileSystemResource;
    import org.springframework.http.MediaType;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.reactive.function.BodyInserters;
    import org.springframework.web.reactive.function.client.WebClient;

    @Service
    public class FileSystemStorageService implements StorageService {

        private final Path rootLocation;
        private final WebClient webClient;

        @Autowired
        public FileSystemStorageService(StorageProperties properties, WebClient webClient) {
            this.webClient = webClient;

            if(properties.getLocation().trim().length() == 0){
                throw new StorageException("File upload location can not be Empty.");
            }

            this.rootLocation = Paths.get(properties.getLocation());
        }

        @Override
        public String store(MultipartFile file) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new StorageException("Please login first!");
            }
            boolean isUser = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
            if (!isUser) {
                throw new StorageException("Only users can upload files.");
            }

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            try {
                Path destinationFile = this.rootLocation.resolve(
                                Paths.get(file.getOriginalFilename()))
                        .normalize().toAbsolutePath();

                if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                    throw new StorageException("Cannot store file outside current directory.");
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                }

                FileSystemResource resource = new FileSystemResource(destinationFile.toFile());

                String apiResponse = webClient.post()
                        .uri("https://api.apilayer.com/resume_parser/upload")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(BodyInserters.fromResource(resource))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                return apiResponse;


            } catch (IOException e) {
                throw new StorageException("Failed to store file.", e);
            } catch (Exception ex) {
                throw new StorageException("Failed to upload file to external API.", ex);
            }
        }



        @Override
        public void init() {
            try {
                Files.createDirectories(rootLocation);
            }
            catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        }


    }
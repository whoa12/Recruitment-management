# Recruitment Management System

A Spring Boot application for managing job postings, user profiles, and applications.

## Features
- User registration and authentication using JWT
- Admins can create and manage job postings
- Users can upload resumes (PDF/DOCX)
- Resume data is parsed via external API
- Users can apply for jobs
- Profile management with skills, education, and experience
- ObjectMapper for parsing Json response into Profile Repository.

## Stack Used
- Java 21
- Spring Boot 3.5.7 (REST APIs, Security)
- Spring Data JPA with MySQL

Endpoints: (Along with screenshots tested on PostMan)

1. User registration

POST /auth/register

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(163).png?raw=true)


2. User login

POST /auth/login

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(164).png?raw=true)

3. Uploading resume. Only Applicant type users can access this API. Taken care using Role based access control.

POST /api/uploadResume

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(166).png?raw=true)

4. Fetching a job with id and applicants who applied on that job. Only admins can do this. Taken care using Role based access control.

GET /api/admin/job/{job_id}

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(167).png?raw=true)


5. Creating a job opening. Only admins.

POST /api/admin/job

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(168).png?raw=true)


6. Applying to a job. Only applicants.

POST /api/jobs/apply?job_id={job_id}

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(170).png?raw=true)

7. Fetching information of an applicant. Only admins.

GET /api/admin/applicant/{applicant_id}

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(172).png?raw=true)

8. Fetching a list of all applicants

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(173).png?raw=true)

9. Getting all job openings. Accessible to all users.

![Screenshot](https://github.com/whoa12/Recruitment-management/blob/main/Screenshot%20(174).png?raw=true)



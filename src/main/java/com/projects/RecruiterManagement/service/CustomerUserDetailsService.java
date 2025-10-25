package com.projects.RecruiterManagement.service;


import com.projects.RecruiterManagement.model.User;
import com.projects.RecruiterManagement.model.User_Role;
import com.projects.RecruiterManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("Not found!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        User_Role role = user.getRole();
        if(role==null){
            role = User_Role.ROLE_USER;
        }
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}

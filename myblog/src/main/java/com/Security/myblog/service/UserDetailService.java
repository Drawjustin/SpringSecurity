package com.Security.myblog.service;

import com.Security.myblog.domain.User;
import com.Security.myblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException((email)));
    }

}

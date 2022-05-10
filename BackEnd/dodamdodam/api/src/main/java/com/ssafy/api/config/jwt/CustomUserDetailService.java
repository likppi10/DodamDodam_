package com.ssafy.api.config.jwt;

import com.ssafy.core.entity.User;
import com.ssafy.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepository.getUserByUserPk(Long.parseLong(userPk));
        } catch (Exception e) {
            log.debug("Error in loadUserByUsername Time: {}, Message: {}", LocalDateTime.now(), e.getMessage());
        }
        return user;
    }
}
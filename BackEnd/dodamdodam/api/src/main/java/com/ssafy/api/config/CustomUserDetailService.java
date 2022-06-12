package com.ssafy.api.config;

import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.ssafy.core.exception.ErrorCode.USER_DOESNT_EXIST;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk) {
        User user = null;
        try {
            user = userRepository.findUserByUserPk(Long.parseLong(userPk))
                    .orElseThrow(() -> new CustomException(USER_DOESNT_EXIST));
        } catch (Exception e) {
            log.debug("Error in loadUserByUsername Time: {}, Message: {}", LocalDateTime.now(), e.getMessage());
        }
        return user;
    }
}
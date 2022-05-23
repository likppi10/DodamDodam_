package com.ssafy.core.repository;

import com.ssafy.core.entity.User;
import com.ssafy.core.repository.querydsl.UserRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepoCustom {

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByUserPk(Long userPk);

    Boolean existsUserByUserId(String userId);

}

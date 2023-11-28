package com.ssuopenpj.spring;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByPhone(String phone);
    UserEntity findByEmail(String email);
    UserEntity getByUserId(String userId);
    UserEntity getByPhone(String phone);
    UserEntity getByEmail(String email);
}
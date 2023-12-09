package com.ssuopenpj.spring.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByPhone(String phone);
    UserEntity findByEmail(String email);
    UserEntity findByNameAndPhoneAndEmail(String name, String phone, String email);
    UserEntity findByUserIdAndPhone(String userId, String Phone);
    UserEntity getByUserIdAndPhone(String userId, String phone);
    UserEntity getByUserId(String userId);
    UserEntity getByPhone(String phone);
    UserEntity getByEmail(String email);
}

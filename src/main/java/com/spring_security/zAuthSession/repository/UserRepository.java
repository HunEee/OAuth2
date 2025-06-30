package com.spring_security.zAuthSession.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring_security.zAuthSession.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>   {

	UserEntity findByUsername(String username);
	
}

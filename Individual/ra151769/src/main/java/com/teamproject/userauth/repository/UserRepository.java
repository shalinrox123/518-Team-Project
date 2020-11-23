package com.teamproject.userauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.teamproject.userauth.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);
}

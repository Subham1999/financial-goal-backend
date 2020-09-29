package com.subham.springfirst.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subham.springfirst.model.User;

public interface UserRepository extends JpaRepository<User, String>{

}

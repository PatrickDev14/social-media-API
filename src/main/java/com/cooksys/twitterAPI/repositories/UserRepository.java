package com.cooksys.twitterAPI.repositories;

import com.cooksys.twitterAPI.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

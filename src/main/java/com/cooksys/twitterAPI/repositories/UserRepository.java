package com.cooksys.twitterAPI.repositories;

import com.cooksys.twitterAPI.entities.Credentials;
import com.cooksys.twitterAPI.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCredentialsUsername(String username);

    List<User> findAllByDeletedFalse();

    Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);

    Optional<User> findByCredentialsAndDeletedFalse(Credentials credentials);

}

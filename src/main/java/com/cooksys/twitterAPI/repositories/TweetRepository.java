package com.cooksys.twitterAPI.repositories;

import com.cooksys.twitterAPI.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

}

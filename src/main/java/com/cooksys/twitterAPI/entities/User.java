package com.cooksys.twitterAPI.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Credentials credentials;

    @Embedded
    private Profile profile;

    private Timestamp joined;

    private boolean deleted;

    @ManyToMany
    @JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private Set<Tweet> likedTweets;

    @ManyToMany
    @JoinTable(name = "user_mentions", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private Set<Tweet> mentionedTweets;

    @ManyToMany
    @JoinTable(name = "followers_following", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<User> followers;
}

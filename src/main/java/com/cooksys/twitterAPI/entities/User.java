package com.cooksys.twitterAPI.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.sql.Timestamp;
import java.util.HashSet;
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

    @ManyToMany(mappedBy =  "user_likes")
    private Set<Tweet> likedTweets = new HashSet<>();

    @ManyToMany(mappedBy = "user_mentions")
    private Set<Tweet> mentionedTweets = new HashSet<>();

}

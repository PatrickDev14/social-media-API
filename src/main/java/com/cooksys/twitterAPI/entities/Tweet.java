package com.cooksys.twitterAPI.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private Timestamp posted;

    private boolean deleted;

    @Nullable
    private String content;

    @Nullable
    @ManyToOne
    @JoinColumn
    private Tweet inReplyTo;

    @Nullable
    @ManyToOne
    private Tweet repostOf;
}

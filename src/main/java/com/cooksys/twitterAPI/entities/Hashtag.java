package com.cooksys.twitterAPI.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;

	private boolean deleted;

	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	private Timestamp firstUsed;

	@Column(nullable = false)
	private Timestamp lastUsed;

	@ManyToMany(mappedBy = "hashtags")
	@EqualsAndHashCode.Exclude
	private Set<Tweet> tweets;
}

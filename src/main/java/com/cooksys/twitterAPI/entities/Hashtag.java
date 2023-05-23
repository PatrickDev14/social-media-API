package com.cooksys.twitterAPI.entities;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
	
	@Id
	@GeneratedValue
	private Integer id;

	private boolean deleted;
	
	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	@CreationTimestamp
	private Timestamp firstUsed;
	
	@Column(nullable = false)
	@UpdateTimestamp
	private Timestamp lastUsed;

	@ManyToMany(mappedBy = "hashtags")
	@EqualsAndHashCode.Exclude
	private Set<Tweet> tweets = new HashSet<>();
}

package com.cooksys.twitterAPI.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Profile {

	private String firstName;

	private String lastName;

	@Nullable
	private String email;

	private String phone;
}

package com.cooksys.twitterAPI.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HashtagDto {

    private String label;

    private Timestamp firstUsed;

    private Timestamp lastUsed;

}

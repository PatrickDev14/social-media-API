package com.cooksys.twitterAPI.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HashtagDto {

    private String label;

    @CreationTimestamp
    private Date firstUsed;

    @UpdateTimestamp
    private Date lastUsed;

}

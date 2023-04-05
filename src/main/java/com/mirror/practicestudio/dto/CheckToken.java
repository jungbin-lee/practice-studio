package com.mirror.practicestudio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckToken {

    private String _id;

    private String refreshToken;

    private String email;
}

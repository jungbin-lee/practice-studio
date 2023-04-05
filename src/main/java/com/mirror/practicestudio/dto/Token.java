package com.mirror.practicestudio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @JsonIgnore
    private String  _id;
   // private String grantType;
    private String accessToken;
    private String refreshToken;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String msg;
}

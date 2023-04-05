package com.mirror.practicestudio.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    //로그인을 위한 username과 password

    private String email;
    private String password;

}
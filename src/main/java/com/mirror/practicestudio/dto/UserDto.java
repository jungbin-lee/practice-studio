package com.mirror.practicestudio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //회원가입을 위한 정보 username,paddword,email의 길이와객체들

    private String username;


    private String password;


    private String email;

}

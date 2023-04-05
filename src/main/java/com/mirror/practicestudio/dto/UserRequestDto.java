package com.mirror.practicestudio.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
@Getter
@Setter
public class UserRequestDto {

    private String _id;

    private String email;

    private String nickname;

    private String profileImg;

    private String birth;

    private String gender;
}

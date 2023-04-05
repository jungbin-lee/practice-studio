package com.mirror.practicestudio.domain;

//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
//@Data
//@NoArgsConstructor
//@Document(collection ="users")
//public class User implements UserDetails {
//
//    @Id
//    private String _id;
//    @Field("nickname")
//    private String nickname;
//    @Field("email")
//    private String email;
//    @Field("password")
//    private String password;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }
//}

import com.mirror.practicestudio.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;

@Builder
@Getter
@Setter
@Data
@Document(collection ="users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    private String _id;
    @Nonnull
    @Field("email")
    private String email;
    @Field("password")
    @JsonIgnore
    private String password;
    @Field("nickname")
    private String nickname;
    @Field("kakaoProfileImg")
    private String profileImg;
    @Field("birth")
    private String birth;
    @Field("gender")
    private String gender;


    @JsonIgnore
    @Override //계정이 가지고 있는 권한 목록들을 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override //계정의 만료여부 리턴 스프링시큐리티의 기능들
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override//계정의 잠금여부를 리턴
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override//계정의 비번이 만료되었는지 리턴
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override//사용가능한계정인지 리턴
    public boolean isEnabled() {
        return true;
    }

    @Builder //비밀번호 인코딩
    public User(UserDto userDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

      //  this.username = userDto.getUsername();
        this.password = passwordEncoder.encode(userDto.getPassword());
        this.email = userDto.getEmail();
    }
}

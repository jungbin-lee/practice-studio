package com.mirror.practicestudio.domain;

import com.mirror.practicestudio.dto.Token;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection ="refresh_token")
public class RefreshToken {

    @Id
    private String _id;


    @Field("refresh_token")
    private String refreshToken;

    @Field("email")
    private String email;


    public void setRefreshToken(String refreshToken) {
        this.refreshToken =refreshToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void update(Token newToken) {
        this.refreshToken= newToken.getRefreshToken();
        this.email= newToken.getEmail();

    }

//    public void save(Token newToken) {
//        this._id = newToken.get_id();
//        this.refreshToken= newToken.getRefreshToken();
//        this.email= newToken.getEmail();
//    }
}

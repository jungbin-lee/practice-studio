package com.mirror.practicestudio.dto;


import lombok.Data;

@Data
public class KakaoProfile {

    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public class Properties { //(1)
        public String nickname;
        public String profile_image; // 이미지 경로 필드1
        public String thumbnail_image;
    }

    @Data
    public class KakaoAccount { //(2)
        public Boolean profile_nickname_needs_agreement;
        public Boolean profile_image_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public Boolean has_age_range;
        public Boolean   age_range_needs_agreement;
        public String age_range;
        public Boolean birthday_needs_agreement;
        public String birthday;
        public Boolean gender_needs_agreement;
        public String  gender;
        public Boolean   has_gender;
        @Data
        public class Profile {
            public String nickname;
            public String thumbnail_image_url;
            public String profile_image_url; // 이미지 경로 필드2
            public Boolean is_default_image;

        }
    }

}
package com.familring.userservice.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class UserDto implements UserDetails {
    private Long userId;
    private String userKakaoId;
    private String userPassword;
    private String userNickname;
    private Date userBirthDate;
    private String userZodiacSign;
    private FamilyRole userRole;
    private String userFace;
    private String userColor;
    private String userEmotion;
    private String userFcmToken;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userModifiedAt;
    private boolean userIsDeleted;
    private boolean userIsAdmin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userKakaoId;
    }
}


package com.example.demoshop.response.users;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
public class UserTokenDto {
    private String email;
    private String token;

    @Builder
    public UserTokenDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    // Entity -> DTO
    public static UserTokenDto fromEntity(UserDetails user, String token) {
        return UserTokenDto.builder()
                .email(user.getUsername())
                .token(token)
                .build();
    }
}

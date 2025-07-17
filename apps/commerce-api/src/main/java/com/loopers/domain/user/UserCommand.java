package com.loopers.domain.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class UserCommand {
    private String userId;
    private String name;
    private UserEntity.Gender gender;
    private String birth;
    private String email;

    public static UserCommand of(String userId, String name, UserEntity.Gender gender, String birth, String email){
        return new UserCommand(
                userId, name, gender, birth, email
        );
    }
}

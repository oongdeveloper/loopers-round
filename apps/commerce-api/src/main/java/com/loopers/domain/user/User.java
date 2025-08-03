package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "members")
@NoArgsConstructor
@Getter
@ToString
public class User extends BaseEntity {
    @Column(name = "user_id", length = 10)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 2)
    private Gender gender;

    @Column(name = "date_of_birth")
    private String birth;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "point", nullable = false)
    private long point;

    private User(String userId, String name, Gender gender, String birth, String email) {
        this.userId = userId;
        this.userName = name;
        this.gender = gender;
        this.birth = birth;
        this.email = email;
        this.point = 0L;
    }

    public static User from(UserCommand command) {
        UserValidator.validate(command);
        return new User(
                command.userId(),
                command.userName(),
                command.gender(),
                command.birth(),
                command.email()
                );
    }

    public enum Gender{
        M,
        F
    }

    public long charge(long chargePoint){
        if (chargePoint <= 0) throw new CoreException(ErrorType.BAD_REQUEST, "충전 금액은 0원 이상이어야 합니다.");
        return this.point += chargePoint;
    }

    public long deduct(long deductPoint){
        if (deductPoint >= 0) throw new CoreException(ErrorType.BAD_REQUEST, "사용할 포인트는 0원 이상이어야 합니다.");

        if (this.point - deductPoint < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST,"포인트가 부족합니다.");
        }
        return this.point -= deductPoint;
    }
}

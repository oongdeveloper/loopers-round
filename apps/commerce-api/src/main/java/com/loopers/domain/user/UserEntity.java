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
@NoArgsConstructor // TODO. JPA 맵핑 시 사용?
@Getter
@ToString
public class UserEntity extends BaseEntity {
    @Column(name = "user_id", length = 10)
    private String userId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 2)
    private Gender gender;

    @Column(name = "date_of_birth")
    private String birth;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "point", nullable = false)
    private long point;

    @Transient
    private final String USER_ID_PATTERN = "^[0-9a-zA-Z]{1,10}$";
    @Transient
    private final String BIRTH_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    @Transient
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public UserEntity(String userId, String name, Gender gender, String birth, String email) {
        if (userId == null || !userId.matches(USER_ID_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "ID 는 1자 이상 10자 이내의 영문과 숫자로 이루어져야 합니다.");

        // TODO. null 확인 필요.
        if (!birth.matches(BIRTH_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 맞지 않습니다. f) yyyy-MM-dd");

        if (!email.matches(EMAIL_PATTERN))
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다. ex) aa@bb.cc");

        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.email = email;
        this.point = 0L;
    }

    // TODO. public 이 맞나?
    public enum Gender{
        M,
        F
    }

    public long charge(long chargePoint){
        if (chargePoint <= 0) throw new CoreException(ErrorType.BAD_REQUEST, "충전 금액은 0원 이상이어야 합니다.");
        return this.point += chargePoint;
    }
}

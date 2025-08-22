package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "option_name")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionName extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50, unique = true) // 옵션 이름 (색상, 사이즈 등)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    // 양방향 관계 설정 (OptionName -> OptionValue)
    @OneToMany(mappedBy = "optionName")
    private Set<OptionValue> optionValues = new HashSet<>();

    private OptionName(String name, String description){
        validate(name, description);
        this.name = name;
        this.description = description;
    }

    public static OptionName from(String name, String description) {
        return new OptionName(name, description);
    }

    private void validate(String name, String description){
        if (StringUtils.isBlank(name) || name.length() > 50)
            throw new CoreException(ErrorType.BAD_REQUEST, "옵션 이름은 50자 이내여야 합니다.");

        if (StringUtils.isBlank(description) || description.length() > 250)
            throw new CoreException(ErrorType.BAD_REQUEST, "옵션 설명은 250자 이내여야 합니다.");

    }

}

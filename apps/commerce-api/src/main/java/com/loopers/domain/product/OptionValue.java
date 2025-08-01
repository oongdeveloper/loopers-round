package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "option_value")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_option_name_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OptionName optionName;

    @Column(name = "value", nullable = false, length = 100) // 옵션 값 (레드, M, 256GB)
    private String value;

    private OptionValue(String value){
        validate(value);
        this.value = value;
    }

    public static OptionValue from(String value) {
        return new OptionValue(value);
    }

    private void validate(String value){
        if (StringUtils.isBlank(value) || value.length() > 100)
            throw new CoreException(ErrorType.BAD_REQUEST, "옵션 값은 100자 이내여야 합니다.");
    }
}

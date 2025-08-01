package com.loopers.domain.product;


import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "sku_option",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ref_product_sku_id", "ref_option_name_id"}) // 한 SKU에 동일 옵션 이름 중복 방지
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SkuOption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_product_sku_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductSku productSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_option_name_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OptionName optionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_option_value_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OptionValue optionValue;
}

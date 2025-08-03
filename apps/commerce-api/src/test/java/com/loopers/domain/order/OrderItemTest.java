package com.loopers.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {
    private final Long TEST_SKU_ID = 1L;
    private final Long TEST_CATALOG_ID = 100L;
    private final Long INITIAL_QUANTITY = 2L;
    private final BigDecimal INITIAL_UNIT_PRICE = BigDecimal.valueOf(10000);
    private final String INITIAL_PRODUCT_NAME = "테스트 상품";

    @Test
    @DisplayName("OrderItem 생성 시 필드들이 올바르게 초기화되고 총 가격이 계산된다.")
    void testOrderItemCreation() {
        // Given & When
        OrderItem orderItem = OrderItem.of(
                TEST_SKU_ID,
                TEST_CATALOG_ID,
                INITIAL_QUANTITY,
                INITIAL_UNIT_PRICE,
                INITIAL_PRODUCT_NAME
        );

        // Then
        assertThat(orderItem.getProductSkuId()).isEqualTo(TEST_SKU_ID);
        assertThat(orderItem.getProductCatalogId()).isEqualTo(TEST_CATALOG_ID);
        assertThat(orderItem.getQuantity()).isEqualTo(INITIAL_QUANTITY);
        assertThat(orderItem.getOrderItemUnitPrice()).isEqualByComparingTo(INITIAL_UNIT_PRICE);
        assertThat(orderItem.getOrderItemProductName()).isEqualTo(INITIAL_PRODUCT_NAME);
        assertThat(orderItem.getTotalItemPrice()).isEqualByComparingTo(INITIAL_UNIT_PRICE.multiply(BigDecimal.valueOf(INITIAL_QUANTITY))); // 10000 * 2 = 20000
        assertThat(orderItem.getOrder()).isNull(); // 처음 생성 시 Order와 연결되지 않음
    }

    @Test
    @DisplayName("setOrder 메서드로 Order와의 양방향 관계가 설정된다.")
    void testSetOrderMethod() {
        // Given
        OrderItem orderItem = OrderItem.of(
                TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);
        Order order = Order.of("user1", "CREATED"); // Order 엔티티 생성

        // When
        orderItem.setOrder(order); // OrderItem의 setOrder 호출

        // Then
        assertThat(orderItem.getOrder()).isEqualTo(order);
    }

    @Nested
    @DisplayName("updateQuantity 메서드는")
    class UpdateQuantityMethod {

        @Test
        @DisplayName("수량을 변경하고 총 가격을 재계산한다.")
        void shouldUpdateQuantityAndRecalculateTotalPrice() {
            // Given
            OrderItem orderItem = OrderItem.of(
                    TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);
            BigDecimal initialTotalPrice = orderItem.getTotalItemPrice(); // 20000

            Long newQuantity = 5L; // 새로운 수량

            // When
            orderItem.updateQuantity(newQuantity);

            // Then
            assertThat(orderItem.getQuantity()).isEqualTo(newQuantity);
            assertThat(orderItem.getTotalItemPrice()).isEqualByComparingTo(INITIAL_UNIT_PRICE.multiply(BigDecimal.valueOf(newQuantity))); // 10000 * 5 = 50000
            assertThat(orderItem.getTotalItemPrice()).isNotEqualByComparingTo(initialTotalPrice); // 가격이 변경되었는지 확인
        }

        @Test
        @DisplayName("음수 수량으로 변경하려고 하면 IllegalArgumentException을 발생시킨다.")
        void shouldThrowException_whenUpdatingWithNegativeQuantity() {
            // Given
            OrderItem orderItem = OrderItem.of(
                    TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> orderItem.updateQuantity(-1L));
            assertThrows(IllegalArgumentException.class, () -> orderItem.updateQuantity(null)); // null도 허용하지 않음
        }

        @Test
        @DisplayName("수량을 0으로 변경할 수 있고 총 가격이 0이 된다.")
        void shouldSetQuantityToZeroAndTotalPriceToZero() {
            // Given
            OrderItem orderItem = OrderItem.of(
                    TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);

            // When
            orderItem.updateQuantity(0L);

            // Then
            assertThat(orderItem.getQuantity()).isEqualTo(0L);
            assertThat(orderItem.getTotalItemPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("updateUnitPrice 메서드는")
    class UpdateUnitPriceMethod {

        @Test
        @DisplayName("단가를 변경하고 총 가격을 재계산한다.")
        void shouldUpdateUnitPriceAndRecalculateTotalPrice() {
            // Given
            OrderItem orderItem = OrderItem.of(
                    TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);
            BigDecimal initialTotalPrice = orderItem.getTotalItemPrice(); // 20000

            BigDecimal newUnitPrice = BigDecimal.valueOf(12000); // 새로운 단가

            // When
            orderItem.updateUnitPrice(newUnitPrice);

            // Then
            assertThat(orderItem.getOrderItemUnitPrice()).isEqualByComparingTo(newUnitPrice);
            assertThat(orderItem.getTotalItemPrice()).isEqualByComparingTo(newUnitPrice.multiply(BigDecimal.valueOf(INITIAL_QUANTITY))); // 12000 * 2 = 24000
            assertThat(orderItem.getTotalItemPrice()).isNotEqualByComparingTo(initialTotalPrice); // 가격이 변경되었는지 확인
        }

        @Test
        @DisplayName("음수 단가로 변경하려고 하면 IllegalArgumentException을 발생시킨다.")
        void shouldThrowException_whenUpdatingWithNegativeUnitPrice() {
            // Given
            OrderItem orderItem = OrderItem.of(
                    TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> orderItem.updateUnitPrice(BigDecimal.valueOf(-1)));
            assertThrows(IllegalArgumentException.class, () -> orderItem.updateUnitPrice(null)); // null도 허용하지 않음
        }

        @Test
        @DisplayName("단가를 0으로 변경할 수 있고 총 가격이 0이 된다.")
        void shouldSetUnitPriceToZeroAndTotalPriceToZero() {
            // Given
            OrderItem orderItem = OrderItem.of(
                    TEST_SKU_ID, TEST_CATALOG_ID, INITIAL_QUANTITY, INITIAL_UNIT_PRICE, INITIAL_PRODUCT_NAME);

            // When
            orderItem.updateUnitPrice(BigDecimal.ZERO);

            // Then
            assertThat(orderItem.getOrderItemUnitPrice()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(orderItem.getTotalItemPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }
}

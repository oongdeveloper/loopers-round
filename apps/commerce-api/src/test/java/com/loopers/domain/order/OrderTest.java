package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private final String TEST_USER_ID = "user1";
    private final String INITIAL_STATUS = "CREATED";

    @Nested
    @DisplayName("addOrderItem 메서드는")
    class AddOrderItemMethod {

        @Test
        @DisplayName("주문 상품을 추가하고, 총 주문 가격을 업데이트하며, 양방향 관계를 설정한다.")
        void shouldAddOrderItemAndUpdateTotalPriceAndSetBidirectionalRelation() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
            OrderItem item1 = OrderItem.of(
                    10L, 100L, 2L, BigDecimal.valueOf(5000), "상품A"); // totalItemPrice = 10000
            OrderItem item2 = OrderItem.of(
                    20L, 200L, 1L, BigDecimal.valueOf(15000), "상품B"); // totalItemPrice = 15000

            // When
            order.addOrderItem(item1);
            order.addOrderItem(item2);

            // Then
            assertThat(order.getOrderItems()).hasSize(2);
            assertThat(order.getOrderItems()).contains(item1, item2); // 리스트에 추가되었는지 확인

            // 양방향 관계 확인
            assertThat(item1.getOrder()).isEqualTo(order);
            assertThat(item2.getOrder()).isEqualTo(order);

            // 총 가격 업데이트 확인 (10000 + 15000 = 25000)
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(25000));
        }

        @Test
        @DisplayName("null OrderItem을 추가하려고 하면 IllegalArgumentException을 발생시킨다.")
        void shouldThrowException_whenAddingNullOrderItem() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);

            // When & Then
            CoreException exception = assertThrows(CoreException.class, () -> {
                order.addOrderItem(null);
            });
            Assertions.assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("removeOrderItem 메서드는")
    class RemoveOrderItemMethod {

        @Test
        @DisplayName("주문 상품을 제거하고, 총 주문 가격을 업데이트하며, 양방향 관계를 해제한다.")
        void shouldRemoveOrderItemAndUpdateTotalPriceAndUnsetBidirectionalRelation() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
            OrderItem item1 = OrderItem.of(
                    10L, 100L, 2L, BigDecimal.valueOf(5000), "상품A");
            OrderItem item2 = OrderItem.of(
                    20L, 200L, 1L, BigDecimal.valueOf(15000), "상품B");
            order.addOrderItem(item1); // 총 가격 10000
            order.addOrderItem(item2); // 총 가격 25000

            assertThat(order.getOrderItems()).hasSize(2);
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(25000));

            // When
            order.removeOrderItem(item1); // item1 제거

            // Then
            assertThat(order.getOrderItems()).hasSize(1);
            assertThat(order.getOrderItems()).doesNotContain(item1);
            assertThat(order.getOrderItems()).contains(item2);

            // 양방향 관계 해제 확인
            assertThat(item1.getOrder()).isNull();
            assertThat(item2.getOrder()).isEqualTo(order); // 다른 아이템은 그대로

            // 총 가격 업데이트 확인 (25000 - 10000 = 15000)
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(15000));
        }

        @Test
        @DisplayName("존재하지 않는 주문 상품을 제거하려고 하면 아무것도 변경하지 않는다.")
        void shouldNotChange_whenRemovingNonExistingOrderItem() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
            OrderItem item1 = OrderItem.of(
                    10L, 100L, 2L, BigDecimal.valueOf(5000), "상품A");
            order.addOrderItem(item1); // 초기 상태: item1 하나만 있음 (총 가격 10000)

            OrderItem nonExistingItem = OrderItem.of(
                    30L, 300L, 1L, BigDecimal.valueOf(100), "존재하지않는상품"); // 리스트에 없는 아이템

            // When
            order.removeOrderItem(nonExistingItem);

            // Then
            assertThat(order.getOrderItems()).hasSize(1); // 크기 변화 없음
            assertThat(order.getOrderItems()).contains(item1); // item1은 여전히 존재
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000)); // 가격 변화 없음
        }

        @Test
        @DisplayName("null OrderItem을 제거하려고 하면 아무것도 변경하지 않는다.")
        void shouldNotChange_whenRemovingNullOrderItem() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
            OrderItem item1 = OrderItem.of(
                    10L, 100L, 2L, BigDecimal.valueOf(5000), "상품A");
            order.addOrderItem(item1);

            // When
            order.removeOrderItem(null);

            // Then
            assertThat(order.getOrderItems()).hasSize(1);
            assertThat(order.getOrderItems()).contains(item1);
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
        }
    }

    @Nested
    @DisplayName("calculateTotalPrice 메서드는")
    class CalculateTotalPriceMethod {

        @Test
        @DisplayName("주문 상품이 없을 때 총 주문 가격을 0으로 설정한다.")
        void shouldSetZero_whenNoOrderItems() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);

            // When (이미 생성자에서 0으로 초기화되지만, 명시적으로 재계산)
            order.calculateTotalPrice();

            // Then
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("주문 상품 추가 후 총 주문 가격을 올바르게 계산한다.")
        void shouldCalculateCorrectly_afterAddingItems() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
            OrderItem item1 = OrderItem.of(10L, 100L, 2L, BigDecimal.valueOf(5000), "상품A"); // 10000
            OrderItem item2 = OrderItem.of(20L, 200L, 1L, BigDecimal.valueOf(15000), "상품B"); // 15000

            // When
            order.addOrderItem(item1); // 내부적으로 calculateTotalPrice 호출
            order.addOrderItem(item2); // 내부적으로 calculateTotalPrice 호출

            // Then
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(25000));
        }

        @Test
        @DisplayName("주문 상품 제거 후 총 주문 가격을 올바르게 계산한다.")
        void shouldCalculateCorrectly_afterRemovingItems() {
            // Given
            Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
            OrderItem item1 = OrderItem.of(10L, 100L, 2L, BigDecimal.valueOf(5000), "상품A"); // 10000
            OrderItem item2 = OrderItem.of(20L, 200L, 1L, BigDecimal.valueOf(15000), "상품B"); // 15000
            order.addOrderItem(item1);
            order.addOrderItem(item2);
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(25000));

            // When
            order.removeOrderItem(item1); // 내부적으로 calculateTotalPrice 호출

            // Then
            assertThat(order.getTotalOrderPrice()).isEqualByComparingTo(BigDecimal.valueOf(15000));
        }
    }

    @Test
    @DisplayName("updateStatus 메서드는 주문 상태를 올바르게 변경한다.")
    void shouldUpdateStatusCorrectly() {
        // Given
        Order order = Order.of(TEST_USER_ID, INITIAL_STATUS);
        String newStatus = "COMPLETED";

        // When
        order.updateStatus(newStatus);

        // Then
        assertThat(order.getStatus()).isEqualTo(newStatus);
    }
}

package com.loopers.domain.order;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderIntegrationTest {

//    @Autowired
//    private OrderFacade orderFacade;
//
//    @Autowired
//    private DatabaseCleanUp databaseCleanUp; // DB 초기화 헬퍼
//
//    @Autowired private ProductRepository productCatalogRepository;
//    @Autowired private ProductSkuRepository productSkuRepository;
//    @Autowired
//    private ProductJpaRepository jpaRepository;
//    @Autowired private StockRepository stockRepository;
//    @Autowired private PointRepository pointRepository;
//
//    private TestOrderHelper helper;
////    private final String TEST_USER_ID = "user1";
//    private final Long TEST_USER_ID = 1L;
//
//    @BeforeEach
//    void setUp() {
//        helper = new TestOrderHelper(productCatalogRepository, productSkuRepository, stockRepository, pointRepository);
//    }
//
//    @AfterEach
//    void tearDown() {
//        databaseCleanUp.truncateAllTables();
//    }
//
//    @Nested
//    @DisplayName("주문 을 할 때,")
//    class OrderCreate {
//
//        @Test
//        @DisplayName("재고가 부족하면 Bad Request 오류를 반환한다.")
//        @Transactional
//        void returnBadRequst_whenOutOfStock() {
//            Product catalog = helper.createProductCatalog("테스트 상품", BigDecimal.valueOf(10000));
//            ProductSku sku = helper.createProductSku(catalog.getId(), ProductSku.SkuStatus.AVAILABLE, BigDecimal.valueOf(10000));
//            Stock stock = helper.createStock(sku.getId(), 1L); // 재고 1개
//            Point point = helper.createPoint(TEST_USER_ID, BigDecimal.valueOf(100000)); // 충분한 포인트
//
//            OrderV1Dto.OrderCreateRequest request = new OrderV1Dto.OrderCreateRequest(
//                    Collections.singletonList(new OrderV1Dto.OrderItemCreateRequest(sku.getId(), 2L)) // 요청 수량 2개
//            );
//
//            OrderCommand.Create create = new OrderCommand.Create(
//                    TEST_USER_ID,
//                    Collections.singletonList(new OrderCommand.ItemCreate(sku.getId(), 2L)),
//                    0L
//            );
//
//            assertThrows(CoreException.class, () -> orderFacade.createOrder(create));
////            assertThrows(CoreException.class, () -> orderFacade.createOrder(TEST_USER_ID, request));
//        }
//
//        @Test
//        @DisplayName("상품이 판매중 상태가 아니면 Conflict 오류를 반환한다.")
//        void returnConflict_whenProductNotAvaliable() {
//            Product catalog = helper.createProductCatalog("테스트 상품", BigDecimal.valueOf(10000));
//            ProductSku sku = helper.createProductSku(catalog.getId(), ProductSku.SkuStatus.DISCONTINUED, BigDecimal.valueOf(10000)); // 판매 중지 상태
//            Stock stock = helper.createStock(sku.getId(), 10L); // 충분한 재고
//            Point point = helper.createPoint(TEST_USER_ID, BigDecimal.valueOf(100000)); // 충분한 포인트
//
//            OrderCommand.Create create = new OrderCommand.Create(
//                    TEST_USER_ID,
//                    Collections.singletonList(new OrderCommand.ItemCreate(sku.getId(), 2L)),
//                    0L
//            );
//
//
//            assertThrows(CoreException.class, () -> orderFacade.createOrder(create));
//        }
//
//        @Test
//        @DisplayName("사용자의 포인트가 부족하면 Conflict 오류를 반환한다.")
//        void returnConflict_whenInsufficientPoint() {
//            Product catalog = helper.createProductCatalog("테스트 상품", BigDecimal.valueOf(10000));
//            ProductSku sku = helper.createProductSku(catalog.getId(), ProductSku.SkuStatus.AVAILABLE, BigDecimal.valueOf(10000));
//            Stock stock = helper.createStock(sku.getId(), 10L);
//            Point point = helper.createPoint(TEST_USER_ID, BigDecimal.valueOf(10000));
//
//            OrderCommand.Create create = new OrderCommand.Create(
//                    TEST_USER_ID,
//                    Collections.singletonList(new OrderCommand.ItemCreate(sku.getId(), 2L)),
//                    0L
//            );
//
//            assertThrows(CoreException.class, () -> orderFacade.createOrder(create));
//        }
//    }
//
//    @Nested
//    @DisplayName("상품을 조회할 때")
//    class OrderRead{
//
//        @Test
//        @DisplayName("상품 상세 조회 시, 없는 주문정보면 Bad Request 오류를 반환한다.")
//        void returnBadRequest_whenOrderNotFound(){
//            Long nonExistingOrderId = 9999L;
//
//            CoreException exception = assertThrows(CoreException.class,
//                    () -> orderFacade.getOrderDetail(
//                            OrderQuery.Detail.of(nonExistingOrderId)
//                    )
//            );
//
//            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
//        }
//
//    }
//
//    /**
//     * 테스트 엔티티 생성을 돕는 헬퍼 클래스 (통합 테스트의 가독성 향상 목적)
//     * 이 클래스는 실제 Repository를 주입받아 사용합니다.
//     */
//    private static class TestOrderHelper {
//        private final ProductRepository productCatalogRepository;
//        private final ProductSkuRepository productSkuRepository;
//        private final StockRepository stockRepository;
//        private final PointRepository pointRepository;
//
//        public TestOrderHelper(ProductRepository productCatalogRepository, ProductSkuRepository productSkuRepository, StockRepository stockRepository, PointRepository pointRepository) {
//            this.productCatalogRepository = productCatalogRepository;
//            this.productSkuRepository = productSkuRepository;
//            this.stockRepository = stockRepository;
//            this.pointRepository = pointRepository;
//        }
//
//        private void setEntityId(BaseEntity entity, Long id) {
//            try {
//                java.lang.reflect.Field idField = BaseEntity.class.getDeclaredField("id");
//                idField.setAccessible(true);
//                idField.set(entity, id);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                throw new RuntimeException("BaseEntity의 id 필드를 설정할 수 없습니다.", e);
//            }
//        }
//
//        public Product createProductCatalog(String productName, BigDecimal basePrice) {
//            Product catalog = Product.from(
//                    1L, productName, basePrice, "http://image.url", "description"
//            );
//            return jpaRepository.save(catalog);
//        }
//
//        public ProductSku createProductSku(Long catalogId, ProductSku.SkuStatus status, BigDecimal unitPrice) {
//            ProductSku sku = ProductSku.from(
//                    Collections.emptyList(), "http://sku.image.url", unitPrice, status, catalogId
//            );
//            return productSkuRepository.save(sku);
//        }
//
//        public Stock createStock(Long skuId, Long quantity) {
//            Stock stock = Stock.from(skuId, quantity);
//            return stockRepository.save(stock);
//        }
//
//        public Point createPoint(Long userId, BigDecimal pointAmount) {
//            Point point = Point.from(userId, pointAmount);
//            return pointRepository.save(point);
//        }
//    }

}

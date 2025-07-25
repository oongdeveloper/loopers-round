# 02-sequence-diagrams

## 상품

### [유저 스토리]

- **브랜드 정보를 조회할 수 있다.**

```mermaid
sequenceDiagram
	participant U as User
	participant BC as BrandController
	participant BS as BrandService
	participant PS as ProductDefinitionService
	
	U ->> BC: GET /api/v1/brands/{brandId}
	BC ->> BS: 브랜드 정보 조회()
	alt 조회 실패 (브랜드 없음)
		BC -->> BS: 404 Not Found
	else 조회 성공
		BS -->> BC: 브랜드 정보
		BC ->> PS: 제품 정보 조회() %% 브랜드의 상품 5개 가져오기.
		PS -->> BC: 상품 정보
	end
```

### [유저 스토리]

- **상품 목록을 조회할 수 있다.**
- **상품 목록 조회 시, 필터링과 정렬 기능을 사용할 수 있다.**

```mermaid
sequenceDiagram
	participant U as User
	participant PC as ProductController
	participant PS as ProductDefinitionService
	
	U ->> PC: GET /api/v1/products/{brandIds[]}/{sortBy}
	%% 필터링 및 정렬 조건 있음.
	PC ->> PS: 상품 목록 조회()	
	PS -->> PC: 상품 목록 정보
```

### [유저 스토리]

- **상품 상세 정보를 조회할 수 있다.**

```mermaid
sequenceDiagram
	participant U as User
	participant PC as ProductController
	participant PF as ProductFacade
	participant PD as ProductDefinitionService
	participant BS as BrandService
	participant PS as ProductSkuService
	
	U ->> PC: GET /api/v1/products/{productId}
	PC ->> PF: 상품 Detail 조회()
	PF ->> PD: 상품 정보 조회(상품)
	alt 조회 실패 (상품 미존재)
		PD -->> PF: 404 Not Found
	else 조회 성공
		PF ->> BS: 브랜드 정보 조회(상품)
		alt 브랜드 정보 없음
			PS -->> PF: 404 Not Found
		else 브랜드 정보 있음
			BS ->> PF: 브랜드 정보
			PF ->> PS: 상품 Detail 조회(상품)
			PS -->> PF: 상품 Detail
		end
	end
```

## 좋아요

### [유저 스토리]

- **상품에 좋아요를 등록한다.**

```mermaid
sequenceDiagram
	participant U as User
	participant LC as LikeController
	participant US as UserService
	participant LF as LikeFacade
	participant PS as ProductService
	participant LS as LikeService
		
	U ->> LC: POST /api/v1/products/{productId}/likes
	LC ->> US: 사용자 인증 (X-USER-ID 헤더)
	alt 인증 실패
		US -->> LC: 401 Unauthorized
	end
	LC ->> LF: 좋아요 등록 요청()
	LF ->> PS: 상품 정보 조회()
	alt 조회 실패 (제품 없음)
		PS -->> LF: 404 Not Found
	else 조회 성공
		LF ->> LS: 좋아요 이력 조회()
		alt 조회 실패 (좋아요 이력 없음)
			LF ->> LS: 좋아요 등록()
		else 조회 성공
			LS -->> LF: No Action
		end
	end
```

### [유저 스토리]

- 좋아요를 취소할 수 있다.

```mermaid
sequenceDiagram
	participant U as User
	participant LC as LikeController
	participant US as UserService
	participant LS as LikeService
		
	U ->> LC: DELETE /api/v1/products/{productId}/likes
	LC ->> US: 사용자 인증 (X-USER-ID 헤더)
	alt 인증 실패
		US -->> LC: 401 Unauthorized
	end
	LC ->> LS: 좋아요 정보 조회()
	alt 조회 실패 (좋아요 이력 없음)
		LS -->> LC: No Action
	else 조회 성공
		LC ->> LS: 좋아요 삭제()
	end
```

### [유저 스토리]

- **내가 좋아요 한 상품 목록을 조회한다.**

```mermaid
sequenceDiagram
	participant U as User
	participant LC as LikeController
	participant US as UserService
	participant LF as LikeFacade
	participant LS as LikeService
	participant PS as ProductService
	
	%% userId 를 삭제
	U ->> LC: GET /api/v1/likes
	LC ->> US: 사용자 인증 (X-USER-ID 헤더)
	alt 인증 실패
		US -->> LC: 401 Unauthorized
	end
	LC ->> LF: 좋아요 목록 조회 요청
	LF ->> LS: 좋아요 목록 조회(유저)
	opt 좋아요한 목록 있는 경우
		LS -->> LF: 좋아요한 상품 목록
		LF ->> PS: 상품 정보 조회()
		PS -->> LF: 상품 정보
	end
```

## 주문

### [유저 스토리]

- **사용자는 주문을 할 수 있다.**

```mermaid
sequenceDiagram
	participant U as User
	participant OC as OrderController
	participant US as UserService
	participant OF as OrderFacade
	participant SS as StockService
	participant PS as PaymentService
	participant TS as PointService
	participant OS as OrderService

	
	U ->> OC: POST /api/v1/orders
	OC ->> US: 사용자 인증 (X-USER-ID 헤더)
	alt 인증 실패
		US -->> OC: 401 Unauthorized
	end
	OC ->> OF : 주문 요청(유저, 상품[])
	OF ->> SS: 상품 재고 차감(상품, 수량)
	alt 재고가 없음
		SS -->> OF: 400 Bad Request
	else 
		OF ->> PS: 결제 요청(유저, 금액, 결제수단)
		opt 결제 수단이 포인트
			%% 실제 구현 시에는 OrderFacade 에서 조합
			PS ->> TS: 포인트 차감(유저, 금액)
			alt 포인트 부족
				TS -->> OF: 409 Conflict
			end
		end
	end
	OF ->> OS: 주문서 생성
```

### [유저 스토리]

- **사용자의 주문 목록을 조회할 수 있다.**

```mermaid
sequenceDiagram
	participant U as User
	participant OC as OrderController
	participant US as UserService
	participant OF as OrderFacade
	participant OS as OrderService
	participant PS as PaymentService
	
	U ->> OC: GET /api/v1/orders
	OC ->> US: 사용자 인증 (X-USER-ID 헤더)
	alt 인증 실패
		US -->> OC: 401 Unauthorized
	end
	OC ->> OF: 주문 목록 조회 요청()
	OF ->> OS: 주문 목록 조회(유저)
	opt 주문 목록이 있는 경우
		OS -->> OF: 주문 정보
		OF ->> PS: 결제 정보 조회(주문)
		PS -->> OF: 결제 정보
	end
```

### [유저 스토리]

- **단일 주문 상세 조회를 할 수 있다.**

```mermaid
sequenceDiagram
	participant U as User
	participant OC as OrderController
	participant US as UserService
	participant OF as OrderFacade
	participant OS as OrderService
	participant PS as PaymentService
	
	U ->> OC: GET /api/v1/orders/{orderId}
	OC ->> US: 사용자 인증 (X-USER-ID 헤더)
	alt 인증 실패
		US -->> OC: 401 Unauthorized
	end
	OC ->> OF: 주문 Detail 조회 요청()
	OF ->> OS: 주문 Detail 조회(주문)
	alt 주문 정보 없음
		OS -->> OF: 404 Not Found
	else 주문 정보 있음
		%% Order 에서 주문 시, 상품 정보 Snapshot 을 가진다.
		OS -->> OF: 주문 Detail & 상품 정보
		OF ->> PS: 결제 정보 조회(주문)
		PS -->> OF: 결제 정보
	end
```
# 키친포스

## 요구 사항

### 상품
- 상품을 등록한다.
  - 상품 등록시 가격이 0원 미만이면 예외를 던진다.
- 상품 목록을 조회한다.

### 메뉴 그룹
- 메뉴 그룹을 등록한다.
- 메뉴 그룹 목록을 조회한다.

### 메뉴
- 메뉴를 등록한다.
    - 메뉴 등록시 메뉴 가격이 0원 이상이 아니면 예외를 던진다.
    - 메뉴 등록시 메뉴 그룹이 존재하지 않으면 예외를 던진다.
    - 메뉴 등록시 메뉴에 존재하지 않는 상품이 있다면 예외를 던진다.
    - 메뉴 등록시 메뉴 상품들의 가격 합계가 메뉴 가격보다 크다면 예외를 던진다.
- 메뉴 목록을 조회한다.

### 주문
- 주문을 등록한다.
    - 주문 등록시 주문 항목이 존재하지 않으면 예외를 던진다.
    - 주문 등록시 메뉴가 존재하지 않으면 예외를 던진다.
    - 주문 등록시 주문 테이블이 비어있으면 예외를 던진다.
- 주문 목록을 조회한다.
- 주문 상태를 수정한다.
    - 주문 상태 수정시 주문이 존재하지 않으면 예외를 던진다.
    - 주문 상태 수정시 주문의 상태가 '계산 완료' 상태라면 예외를 던진다.

### 주문 테이블
- 주문 테이블을 등록한다.
- 주문 테이블 목록을 조회한다.
- 주문 테이블 비움 상태를 수정한다.
  - 주문 테이블 비움 상태 수정시 주문 테이블이 존재하지 않으면 예외를 던진다.
  - 주문 테이블 비움 상태 수정시 단체 지정 테이블이면 예외를 던진다.
  - 주문 테이블 비움 상태 수정시 주문 테이블의 주문 상태가 '조리' 또는 '식사'라면 예외를 던진다.
- 테이블을 방문한 손님 수를 수정한다.
  - 테이블을 방문한 손님 수를 수정시 손님수가 0이상이 아니라면 예외를 던진다.
  - 테이블을 방문한 손님 수를 수정시 주문 테이블이 존재하지 않으면 예외를 던진다.

### 단체 지정
- 단체 지정을 등록한다.
    - 단체 지정 등록시 주문 테이블이 존재하지 않으면 예외를 던진다.
    - 단체 지정 등록시 주문 테이블 개수가 2개 미만이라면 예외를 던진다.
    - 단체 지정 등록시 주문 테이블이 비어있지 않으면 예외를 던진다.
    - 단체 지정 등록시 이미 단체 지정된 주문 테이블이 존재하면 예외를 던진다.
- 단체 지정을 해제한다.
    - 단체 지정 해제시 주문 테이블의 주문 상태가 '조리' 또는 '식사'라면 예외를 던진다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

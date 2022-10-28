package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    private OrderTable 주문_테이블;

    private Order 요리중_주문;

    @BeforeEach
    void setUp() {
        주문_테이블 = tableService.create(new OrderTableRequest( 3, false));
        요리중_주문 = new Order(주문_테이블, OrderStatus.COOKING.name(), LocalDateTime.now());
    }

    @Test
    void 주문을_생성할_수_있다() {
        주문_항목을_추가한다(요리중_주문);

        final List<OrderLineItemRequest> orderLineItemRequests = 요리중_주문.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(),
                요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime(),
                orderLineItemRequests);

        Order targetOrder = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(targetOrder.getId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderStatus()).isEqualTo(targetOrder.getOrderStatus()),
                () -> assertThat(targetOrder.getOrderTable().getId()).isNotNull(),
                () -> assertThat(targetOrder.getOrderLineItems().size()).isEqualTo(2)
        );
    }

    @Test
    void 주문을_생성할_때_주문_항목이_없으면_예외가_발생한다() {
        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(), 요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime());

        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목_수와_메뉴_수가_같지_않으면_예외가_발생한다() {
        Menu ramen = 메뉴를_생성한다("라면");
        요리중_주문.addOrderLineItem(new OrderLineItem(요리중_주문, ramen.getId(), 1));
        요리중_주문.addOrderLineItem(new OrderLineItem(요리중_주문, ramen.getId(), 1));

        final List<OrderLineItemRequest> orderLineItemRequests = 요리중_주문.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(), 요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime(), orderLineItemRequests);

        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문에_적혀있는_주문_테이블이_존재하지_않으면_예외가_발생한다() {
        주문_항목을_추가한다(요리중_주문);

        assertThatThrownBy(
                () -> orderService.create(new OrderRequest(0L, OrderStatus.COOKING.name(), LocalDateTime.now()))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_테이블이_비어있으면_예외가_발생한다() {
        주문_테이블 = tableService.create(new OrderTableRequest( 3, true));
        요리중_주문.setOrderTable(주문_테이블);
        주문_항목을_추가한다(요리중_주문);

        final List<OrderLineItemRequest> orderLineItemRequests = 요리중_주문.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(), 요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime(), orderLineItemRequests);

        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회할_수_있다() {
        주문_항목을_추가한다(요리중_주문);

        final List<OrderLineItemRequest> orderLineItemRequests = 요리중_주문.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(), 요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime(), orderLineItemRequests);

        orderService.create(orderRequest);

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isOne(),
                () -> assertThat(orders.get(0).getOrderTable().getId()).isEqualTo(주문_테이블.getId()),
                () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(요리중_주문.getOrderStatus())
        );
    }

    @Test
    void 주문을_변경할_수_있다() {
        주문_항목을_추가한다(요리중_주문);

        final List<OrderLineItemRequest> orderLineItemRequests = 요리중_주문.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(), 요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime(), orderLineItemRequests);

        요리중_주문 = orderService.create(orderRequest);
        Order targetOrder = orderService.changeOrderStatus(요리중_주문.getId(), new Order(주문_테이블,
                OrderStatus.MEAL.name(), LocalDateTime.now()));

        assertThat(targetOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 존재하지_않는_주문을_변경하려_하면_예외가_발생한다() {
        주문_항목을_추가한다(요리중_주문);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(null, new Order(주문_테이블,
                        OrderStatus.MEAL.name(), LocalDateTime.now()))
        );
    }

    @Test
    void 배달이_완료된_상태에서_주문을_변경하려_하면_예외가_발생한다() {
        주문_항목을_추가한다(요리중_주문);

        final List<OrderLineItemRequest> orderLineItemRequests = 요리중_주문.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());

        final OrderRequest orderRequest = new OrderRequest(요리중_주문.getOrderTable().getId(), OrderStatus.COMPLETION.name(),
                요리중_주문.getOrderedTime(), orderLineItemRequests);

        Order targetOrder = orderService.create(orderRequest);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(targetOrder.getId(), targetOrder)
        );
    }
}

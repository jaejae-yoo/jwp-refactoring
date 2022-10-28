package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TableService tableService;

    protected void 주문_항목을_추가한다(Order order) {
        Menu ramen = 메뉴를_생성한다("라면");
        Menu chapagetti = 메뉴를_생성한다("짜파게티");
        order.addOrderLineItem(new OrderLineItem(order, ramen.getId(), 1));
        order.addOrderLineItem(new OrderLineItem(order, chapagetti.getId(), 1));
    }

    protected Menu 메뉴를_생성한다(String name) {
        Product product = productService.create(new ProductRequest("맛있는 라면", new BigDecimal(1300)));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(null, product.getId(), 1));

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("면"));
        Menu ramen = menuService.create(new MenuRequest(name, new BigDecimal(1200), menuGroup.getId(), menuProducts));
        menuProducts.get(0).setMenu(ramen);

        return ramen;
    }

    protected List<OrderTable> 주문_테이블들(boolean firstTableEmpty, boolean secondTableEmpty) {
        OrderTable 주문_테이블1 = tableService.create(new OrderTableRequest(3, firstTableEmpty));
        OrderTable 주문_테이블2 = tableService.create(new OrderTableRequest( 4, secondTableEmpty));

        return Arrays.asList(주문_테이블1, 주문_테이블2);
    }
}

package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.menugroup.MenuGroup;

public class DomainFixtures {

    private DomainFixtures() {
    }

    public static ProductRequest 맛있는_라면() {
        return new ProductRequest("맛있는 라면", new BigDecimal(1300));
    }

    public static MenuRequest 라면_메뉴() {
        return new MenuRequest("라면", new BigDecimal(1200), 1L, new ArrayList<>());
    }

    public static MenuGroup 면_메뉴_그룹() {
        return new MenuGroup("면");
    }

    public static MenuGroupRequest 면_메뉴_그룹_요청() {
        return new MenuGroupRequest("면");
    }

    public static OrderTableRequest 빈_주문_테이블_3인() {
        return new OrderTableRequest( 3, true);
    }

    public static OrderTableRequest 빈_주문_테이블_4인() {
        return new OrderTableRequest( 4, true);
    }

    public static OrderTableRequest 주문_테이블_3인() {
        return new OrderTableRequest(3, false);
    }

    public static OrderTableRequest 주문_테이블_4인() {
        return new OrderTableRequest(4, false);
    }
}

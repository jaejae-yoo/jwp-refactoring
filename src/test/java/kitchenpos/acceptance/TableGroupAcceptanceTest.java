package kitchenpos.acceptance;

import static kitchenpos.DomainFixtures.빈_주문_테이블_3인;
import static kitchenpos.DomainFixtures.빈_주문_테이블_4인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 단체_테이블을_지정할_수_있다() {
        OrderTableRequest 테이블_3인 = 빈_주문_테이블_3인();
        OrderTable 테이블1 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);

        OrderTableRequest 테이블_4인 = 빈_주문_테이블_4인();
        OrderTable 테이블2 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_4인,
                OrderTable.class);

        List<OrderTable> 주문_테이블들 = new ArrayList<>();
        주문_테이블들.add(테이블1);
        주문_테이블들.add(테이블2);

        final List<Long> 주문_테이블들_ID = 주문_테이블들.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        TableGroupRequest 테이블_그룹 = new TableGroupRequest(LocalDateTime.now(), 주문_테이블들_ID);
        TableGroup target = testRestTemplate.postForObject("http://localhost:" + port + "/api/table-groups",
                테이블_그룹, TableGroup.class);

        assertThat(target.getId()).isNotZero();
    }

    @Test
    void 단체_테이블을_해제_할_수_있다() {
        OrderTableRequest 테이블_3인 = 빈_주문_테이블_3인();
        OrderTable 테이블1 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_3인,
                OrderTable.class);

        OrderTableRequest 테이블_4인 = 빈_주문_테이블_4인();
        OrderTable 테이블2 = testRestTemplate.postForObject("http://localhost:" + port + "/api/tables", 테이블_4인,
                OrderTable.class);

        List<OrderTable> 주문_테이블들 = new ArrayList<>();
        주문_테이블들.add(테이블1);
        주문_테이블들.add(테이블2);

        TableGroup 테이블_그룹 = new TableGroup(LocalDateTime.now(), 주문_테이블들);
        TableGroup 저장된_테이블_그룹 = testRestTemplate.postForObject("http://localhost:" + port + "/api/table-groups",
                테이블_그룹, TableGroup.class);

        assertDoesNotThrow(
                () -> testRestTemplate.delete("http://localhost:" + port + "/api/table-groups/" + 저장된_테이블_그룹.getId(),
                Void.class)
        );
    }
}

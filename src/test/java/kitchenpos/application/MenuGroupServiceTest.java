package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.menugroup.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_저장한다() {
        MenuGroup noodle = new MenuGroup("면");
        MenuGroup menuGroup = menuGroupService.create(noodle);

        assertThat(menuGroup.getName()).isEqualTo("면");
    }

    @Test
    void 메뉴_그룹의_여러_메뉴를_조회할_수_있다() {
        menuGroupService.create(new MenuGroup("면"));
        menuGroupService.create(new MenuGroup( "피자"));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups.size()).isEqualTo(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo("면"),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo("피자")
        );
    }
}

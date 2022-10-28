package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @JsonIgnore
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts, List<Product> products) {
        this(null, name, price, menuGroupId, menuProducts, products);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts,
                List<Product> products) {
        validatePrice(price);
        validatePriceWithProducts(price, menuProducts, products);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴 가격이 null이면 예외가 발생한다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격이 0보다 작으면 예외가 발생한다.");
        }
    }

    private void validatePriceWithProducts(final BigDecimal price, List<MenuProduct> menuProducts,
                                           List<Product> products) {

        final List<Long> productIds = mapToProductIds(menuProducts);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Long productId = getProductIdByMenuProduct(productIds, menuProduct);

            final Product product = getProductById(products, productId);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private Long getProductIdByMenuProduct(List<Long> productIds, MenuProduct menuProduct) {
        return productIds.stream()
                .filter(id -> id.equals(menuProduct.getProductId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("product를 찾을 수 없습니다."));
    }

    private Product getProductById(List<Product> products, Long productId) {
        return products.stream()
                .filter(it -> it.getId().equals(productId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Long> mapToProductIds(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

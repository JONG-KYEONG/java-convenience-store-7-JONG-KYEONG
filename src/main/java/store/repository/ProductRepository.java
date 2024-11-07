package store.repository;

import java.util.List;
import store.domain.Product;

public class ProductRepository {
    private static ProductRepository instance;
    private final List<Product> products;

    private ProductRepository(List<Product> products){
        this.products = products;
    }

    public static ProductRepository getInstance() {
        return instance;
    }

    public static void init(List<Product> products) {
        if (instance == null) {
            instance = new ProductRepository(products);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void validateName(Product product){
        products.stream()
                .filter(products -> products.name().equals(product.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
    }

    public void validQuantity(Product product){
        int totalQuantity = products.stream()
                .filter(products -> products.name().equals(product.name()))
                .mapToInt(Product::quantity)
                .sum();

        if (totalQuantity < product.quantity()) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

}

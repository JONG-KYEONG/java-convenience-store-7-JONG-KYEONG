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
}

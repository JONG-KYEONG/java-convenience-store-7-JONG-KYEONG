package store.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;

public class ProductServiceTest {
    private ProductService productService = new ProductService();
    @DisplayName("프로모션 없이 구매하는 상품들을 걸러낸다")
    @Test
    void 프로모션_없이_구매하는_상품들을_걸러낸다() {
        List<Product> inputProducts = List.of(
                new Product("콜라", 1000, 3, "프로모션"),
                new Product("사이다", 1200, 2, "null"),
                new Product("에너지바", 1500, 1, "프로모션")
        );

        List<Product> promotionProducts = List.of(
                new Product("콜라", 1000, 5, "프로모션"),
                new Product("에너지바", 1500, 2, "프로모션")
        );

        List<Product> purchaseProducts = productService.getPurchaseProduct(inputProducts, promotionProducts);

        List<Product> expectedProducts = List.of(
                new Product("사이다", 1200, 2, "null")
        );

        assertEquals(expectedProducts, purchaseProducts);
    }
}

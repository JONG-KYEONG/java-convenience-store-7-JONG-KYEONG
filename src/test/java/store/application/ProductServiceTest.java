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
                new Product("콜라", 0, 3, null),
                new Product("사이다", 0, 2, null),
                new Product("에너지바", 0, 1, null)
        );

        List<Product> promotionProducts = List.of(
                new Product("콜라", 0, 5, null),
                new Product("에너지바", 0, 2, null)
        );

        List<Product> purchaseProducts = productService.getPurchaseProduct(inputProducts, promotionProducts);

        List<Product> expectedProducts = List.of(
                new Product("사이다", 0, 2, null)
        );

        assertEquals(expectedProducts, purchaseProducts);
    }

    @DisplayName("프로모션 상품이지만 추가 구매가 필요한 상품들을 걸러낸다")
    @Test
    void 프로모션_상품이지만_추가_구매가_필요한_상품들을_걸러낸다() {
        List<Product> inputProducts = List.of(
                new Product("콜라", 0, 5, null),
                new Product("사이다", 0, 2, null),
                new Product("에너지바", 0, 4, null)
        );

        List<Product> promotionProducts = List.of(
                new Product("콜라", 0, 3, null),
                new Product("에너지바", 0, 2, null)
        );

        List<Product> additionalPurchaseProducts = productService.getPurchaseProductWithoutPromotion(inputProducts,
                promotionProducts);

        List<Product> expectedProducts = List.of(
                new Product("콜라", 0, 2, null),
                new Product("에너지바", 0, 2, null)
        );

        assertEquals(expectedProducts, additionalPurchaseProducts);
    }

}

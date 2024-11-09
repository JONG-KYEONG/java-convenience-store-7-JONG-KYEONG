package store.dto;

import java.util.List;
import store.domain.Product;

public class ProductProcessingResults {
    private List<Product> promotionProduct;
    private List<Product> giftEligibleProducts;
    private List<Product> purchaseProducts;
    private List<Product> purchaseWithPromotionProducts;

    public ProductProcessingResults(List<Product> promotionProduct, List<Product> giftEligibleProducts,
                                    List<Product> purchaseProducts, List<Product> purchaseWithPromotionProducts) {
        this.promotionProduct = promotionProduct;
        this.giftEligibleProducts = giftEligibleProducts;
        this.purchaseProducts = purchaseProducts;
        this.purchaseWithPromotionProducts = purchaseWithPromotionProducts;
    }

    public List<Product> getPromotionProduct() {
        return promotionProduct;
    }

    public List<Product> getGiftEligibleProducts() {
        return giftEligibleProducts;
    }

    public List<Product> getPurchaseProducts() {
        return purchaseProducts;
    }

    public List<Product> getPurchaseWithPromotionProducts() {
        return purchaseWithPromotionProducts;
    }
}
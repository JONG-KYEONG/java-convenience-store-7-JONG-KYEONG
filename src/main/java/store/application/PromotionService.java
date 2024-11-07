package store.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class PromotionService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public PromotionService() {
        this.productRepository = ProductRepository.getInstance();
        this.promotionRepository = PromotionRepository.getInstance();
    }

    public List<Product> getGiftEligibleProducts(List<Product> products) {
        List<Product> giftEligibleProducts = new ArrayList<>();
        for (Product product : products) {
            Optional<Product> promotionProduct = productRepository.findProductByNAmeWithPromotion(product.name());
            if (promotionProduct != null) {
                if (isEligibleForAdditionalGift(product, promotionProduct.get()) && promotionProduct.get().quantity() >= product.quantity()) {
                    giftEligibleProducts.add(product);
                }
            }
        }
        return giftEligibleProducts;
    }

    private boolean isEligibleForAdditionalGift(Product product, Product promotionProduct){
        Promotion promotion = promotionRepository.findValidPromotionByName(promotionProduct.name()).get();
        int quantityGap = product.quantity() % (promotion.get() + promotion.buy());
        if (quantityGap <= promotion.get() && quantityGap != 0) {
            return true;
        }
        return false;
    }

}

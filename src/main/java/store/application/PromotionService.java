package store.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.Receipt;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class PromotionService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public PromotionService() {
        this.productRepository = ProductRepository.getInstance();
        this.promotionRepository = PromotionRepository.getInstance();
    }

    public List<Product> getGiftEligibleProducts(List<Product> products) {  // 고객이 추가로 받을 수 있는 프로모션 상품 리스트 가져오는 메소드
        List<Product> giftEligibleProducts = new ArrayList<>();
        for (Product product : products) {
            Product promotionProduct = productRepository.findProductByNameWithPromotion(product.name()).get();
            giftEligibleProducts = addIfGiftEligible(product, promotionProduct, giftEligibleProducts);
        }
        return giftEligibleProducts;
    }

    public Receipt calculatePromotionPrice(Receipt receipt, List<Product> promotionProducts) { // 프로모션으로 구매하는 상품 영수증에 찍기
        for (Product product : promotionProducts) {
            Product promotionProduct = productRepository.findProductByNameWithPromotion(product.name()).get();
            Promotion promotion = promotionRepository.findValidPromotionByName(promotionProduct.promotion()).get();
            receipt = updateReceiptWithPromotion(receipt, product, promotion, promotionProduct);
        }
        return receipt;
    }

    public List<Product> getPromotionProduct(List<Product> products) {   // 프로모션 해당되는 상품만 리스트로 반환하기
        List<Product> promotionProduct = new ArrayList<>();
        for (Product product : products) {
            Optional<Product> stackProduct = productRepository.findProductByNameWithPromotion(product.name());
            if (stackProduct.isPresent()) {
                addPromotionProduct(product, stackProduct.get(), promotionProduct);
            }
        }
        return promotionProduct;
    }

    public Receipt updateAdditionalPromotion(Receipt receipt,
                                             List<Product> promotionProducts) {  // 추가로 받을 프로모션 상품 영수증에 업데이트
        for (Product product : promotionProducts) {
            Product presentProduct = productRepository.decreasePromotionQuantity(product.name(), 1);
            receipt.updateAdditionalPresentProduct(presentProduct, presentProduct.price());
        }
        return receipt;
    }

    private void addPromotionProduct(Product product, Product stackProduct, List<Product> promotionProduct) {
        Optional<Promotion> optionalPromotion = promotionRepository.findValidPromotionByName(
                stackProduct.promotion());
        if (optionalPromotion.isPresent()) {
            promotionProduct.add(applyPromotionIfEligible(product, stackProduct, optionalPromotion.get()));
        }
    }

    private List<Product> addIfGiftEligible(Product product, Product promotionProduct,
                                            List<Product> giftEligibleProducts) { // 리스트에 추가 제공 프로모션 상품 추가하는 메소드
        if (isEligibleForAdditionalGift(product, promotionProduct)
                && promotionProduct.quantity() > product.quantity()) {
            giftEligibleProducts.add(product);
        }
        return giftEligibleProducts;
    }

    private boolean isEligibleForAdditionalGift(Product product,
                                                Product promotionProduct) {  // 추가로 받을 수 있는 프로모션인지 확인하는 메소드
        Promotion promotion = promotionRepository.findValidPromotionByName(promotionProduct.promotion()).get();
        int quantityGap = product.quantity() % (promotion.get() + promotion.buy());
        if (quantityGap == promotion.buy()) {
            return true;
        }
        return false;
    }


    private Product applyPromotionIfEligible(Product product, Product stackProduct,
                                             Promotion promotion) { // 프로모션보다 많이 살려는 경우면 프로모션 범위 내에서만 확인
        if (product.quantity() > stackProduct.quantity()) {
            int adjustedQuantity =
                    stackProduct.quantity() - (stackProduct.quantity() % (promotion.buy() + promotion.get()));
            return new Product(product, adjustedQuantity);
        }
        return product;
    }

    private Receipt updateReceiptWithPromotion(Receipt receipt, Product product,
                                               Promotion promotion, Product promotionProduct) {  // 프로모션 상품 영수증 업데이트
        productRepository.decreasePromotionQuantity(product.name(), product.quantity());
        int promotionCount = product.quantity() / (promotion.buy() + promotion.get());

        receipt.updatePurchaseProduct(promotionProduct, product.quantity(),
                product.quantity() * promotionProduct.price());
        receipt.updatePresentProducts(promotionProduct, promotionCount * promotion.get());
        return receipt;
    }
}

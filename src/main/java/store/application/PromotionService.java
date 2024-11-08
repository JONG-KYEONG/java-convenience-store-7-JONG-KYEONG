package store.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.PresentProduct;
import store.dto.PurchaseProduct;
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

    public List<Product> getGiftEligibleProducts(List<Product> products) {  // 프로모션 추가로 받을 수 있는 상품 리스트 가져오는 메소드
        List<Product> giftEligibleProducts = new ArrayList<>();
        for (Product product : products) {
            Optional<Product> promotionProduct = productRepository.findProductByNameWithPromotion(product.name());
            if (promotionProduct.isPresent()) {
                addIfGiftEligible(product, promotionProduct.get(), giftEligibleProducts);
            }
        }
        return giftEligibleProducts;
    }

    private void addIfGiftEligible(Product product, Product promotionProduct, List<Product> giftEligibleProducts) { // 리스트에 추가 제공 프로모션 상품 추가하는 메소드
        if (isEligibleForAdditionalGift(product, promotionProduct)
                && promotionProduct.quantity() >= product.quantity()) {
            giftEligibleProducts.add(product);
        }
    }


    private boolean isEligibleForAdditionalGift(Product product, Product promotionProduct) {  // 추가로 받을 수 있는 프로모션인지 확인하는 메소드
        Promotion promotion = promotionRepository.findValidPromotionByName(promotionProduct.name()).get();
        int quantityGap = product.quantity() % (promotion.get() + promotion.buy());
        if (quantityGap <= promotion.get() && quantityGap != 0) {
            return true;
        }
        return false;
    }

    public List<Product> getPromotionProduct(List<Product> products) {   // 프로모션 해당되는 상품만 리스트로 반환하기
        List<Product> promotionProduct = new ArrayList<>();
        for (Product product : products) {
            Optional<Promotion> optionalPromotion = promotionRepository.findValidPromotionByName(product.name());
            Product stackProduct = productRepository.findProductByNameWithPromotion(product.name()).get();
            if (optionalPromotion.isPresent()) {
                promotionProduct.add(applyPromotionIfEligible(product, stackProduct, optionalPromotion.get()));
            }
        }
        return promotionProduct;
    }

    private Product applyPromotionIfEligible(Product product, Product stackProduct,
                                             Promotion promotion) { // 프로모션보다 많이 살려는 경우면 프로모션 범위 내에서만 확인
        if (product.quantity() > stackProduct.quantity()) {
            int adjustedQuantity =
                    stackProduct.quantity() - (stackProduct.quantity() % (promotion.buy() + promotion.get()));
            return new Product(product.name(), product.price(), adjustedQuantity, product.promotion());
        }
        return product;
    }

    public Receipt calculatePromotionPrice(Receipt receipt, List<Product> promotionProducts) {
        for (Product product : promotionProducts) {
            Product promotionProduct = productRepository.findProductByNameWithPromotion(product.name()).get();
            Promotion promotion = promotionRepository.findValidPromotionByName(promotionProduct.name()).get();
            receipt = updateReceiptWithPromotion(receipt, product, promotion);
        }
        return receipt;
    }

    private Receipt updateReceipt(Receipt receipt, Product product, int quantity) {
        productRepository.decreaseQuantity(product.name(), quantity, "null");
        receipt.updatePurchaseProduct(
                new PurchaseProduct(product.name(), quantity, product.price() * quantity));
        return receipt;
    }

    private Receipt updateReceiptWithPromotion(Receipt receipt, Product product, Promotion promotion) {
        productRepository.decreaseQuantity(product.name(), product.quantity(), product.promotion());
        int promotionCount = product.quantity() / (promotion.buy() + promotion.get());
        int purchaseCount = product.quantity() % (promotion.buy() + promotion.get());
        receipt.updatePurchaseProduct(
                new PurchaseProduct(product.name(), promotionCount * promotion.buy() + purchaseCount,
                        (purchaseCount + purchaseCount) * product.price()));
        receipt.updatePresentProducts(new PresentProduct(product.name(), promotionCount * promotion.get()));
        return receipt;
    }


}

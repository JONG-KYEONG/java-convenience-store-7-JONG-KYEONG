package store.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.Receipt;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = ProductRepository.getInstance();
    }

    public List<Product> getStackProduct(){
        return productRepository.getProducts();
    }

    public List<Product> getPurchaseProduct(List<Product> inputProducts,
                                            List<Product> promotionProducts) { // 프로모션 없는 상품.
        List<Product> purchaseProducts = inputProducts.stream()
                .filter(inputProduct -> promotionProducts.stream()
                        .noneMatch(promoProduct -> promoProduct.name().equals(inputProduct.name())))
                .collect(Collectors.toList());
        return purchaseProducts;
    }

    public List<Product> getPurchaseProductWithoutPromotion(List<Product> inputProducts,
                                                            List<Product> promotionProducts) { // 프로모션 상품이었지만 추가로 구매가 필요한 상품.
        return inputProducts.stream()
                .flatMap(inputProduct -> promotionProducts.stream()
                        .filter(promoProduct -> promoProduct.name().equals(inputProduct.name())
                                && promoProduct.quantity() < inputProduct.quantity())
                        .map(promoProduct -> new Product(inputProduct,
                                inputProduct.quantity() - promoProduct.quantity()))
                )
                .collect(Collectors.toList());
    }

    public Receipt calculatePrice(Receipt receipt, List<Product> purchaseProducts, boolean hasMembership) { // 일반 구매 상품 영수증에 찍기
        for (Product product : purchaseProducts) {
            Product purchaseProduct = productRepository.decreaseQuantity(product.name(), product.quantity());
            receipt.updatePurchaseProduct(purchaseProduct, product.quantity(), purchaseProduct.price() * product.quantity());
            if(hasMembership){
                receipt.updateMembershipDiscount(purchaseProduct.price() * product.quantity());
            }
        }
        return receipt;
    }

    public Receipt calculatePriceAdditionalProduct(Receipt receipt, List<Product> purchaseProducts, boolean hasMembership) { // 프로모션 재고 없어서 추가 구매 상품 영수증에 찍기
        for (Product product : purchaseProducts) {
            Product purchaseProduct = productRepository.decreaseQuantity(product.name(), product.quantity());
            receipt.updateAdditionalPurchaseProduct(purchaseProduct, product.quantity(), purchaseProduct.price() * product.quantity());
            if(hasMembership){
                receipt.updateMembershipDiscount(purchaseProduct.price() * product.quantity());
            }
        }
        return receipt;
    }
}

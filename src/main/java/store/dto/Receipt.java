package store.dto;

import java.util.ArrayList;
import java.util.List;
import store.constants.Constants;
import store.domain.Product;

public class Receipt {
    private List<PurchaseProduct> purchaseProducts;
    private List<PresentProduct> presentProducts;
    private int totalCount;
    private int totalAmount;
    private int promotionDiscount;
    private int membershipDiscount;
    private int balanceAmount;

    public Receipt() {
        purchaseProducts = new ArrayList<>();
        presentProducts = new ArrayList<>();
    }

    public void updatePurchaseProduct(Product product, int quantity, int amount) {
        purchaseProducts.add(new PurchaseProduct(product.name(), quantity, amount));
        updateTotalAmount(amount);
    }

    public void updatePresentProducts(Product product, int quantity) {
        presentProducts.add(new PresentProduct(product.name(), quantity));
        updatePromotionDiscount(product.price() * quantity);
    }

    public void updateAdditionalPresentProduct(PresentProduct presentProduct, int amount) {
        presentProducts.stream()
                .filter(product -> product.getName().equals(presentProduct.getName()))
                .findFirst()
                .ifPresent(product -> product.increaseQuantity());
        updatePromotionDiscount(amount);
    }

    public void updateAdditionalPurchaseProduct(Product purchaseProduct, int amount ,int quantity) {
        purchaseProducts.stream()
                .filter(product -> product.getName().equals(purchaseProduct.name()))
                .findFirst()
                .ifPresent(product -> {
                    product.increaseQuantity(quantity);
                    product.updateAmount(amount);
                });
        updateTotalAmount(amount);
    }

    private void updatePromotionDiscount(int amount) {
        this.promotionDiscount += amount;
    }

    private void updateTotalAmount(int amount) {
        this.totalAmount += amount;
    }

    public void updateMembershipDiscount(int amount) {
        int additionalDiscount = (int) (amount * Constants.MEMBERSHIP_DISCOUNT_RATE);
        int updatedDiscount = this.membershipDiscount + additionalDiscount;
        this.membershipDiscount = Math.min(updatedDiscount, Constants.MAX_MEMBERSHIP_DISCOUNT_LIMIT);
    }
}

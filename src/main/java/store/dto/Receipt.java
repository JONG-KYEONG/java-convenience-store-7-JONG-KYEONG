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

    public void updateAdditionalPurchaseProduct(Product purchaseProduct, int amount, int quantity) {
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

    public StringBuilder toStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("==============W 편의점================\n");
        stringBuilder.append("상품명\t\t수량\t금액\n");
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            totalCount += purchaseProduct.getQuantity();
            stringBuilder.append(purchaseProduct.getName() + "\t\t" + purchaseProduct.getQuantity() + " \t"
                    + String.format("%,d", purchaseProduct.getAmount()) + "\n");
        }
        stringBuilder.append("=============증\t정===============\n");
        for (PresentProduct presentProduct : presentProducts) {
            stringBuilder.append(presentProduct.getName() + "\t\t" + presentProduct.getQuantity() + "\n");
        }
        stringBuilder.append("====================================\n");
        stringBuilder.append("총구매액\t\t" + totalCount + "\t" + String.format("%,d", totalAmount) + "\n");
        stringBuilder.append("행사할인\t\t\t-" + String.format("%,d", promotionDiscount) + "\n");
        stringBuilder.append("멤버십할인\t\t\t-" + String.format("%,d", membershipDiscount) + "\n");
        balanceAmount = totalAmount - promotionDiscount - membershipDiscount;
        stringBuilder.append("내실돈\t\t\t " + String.format("%,d", balanceAmount) + "\n");
        return stringBuilder;
    }
}

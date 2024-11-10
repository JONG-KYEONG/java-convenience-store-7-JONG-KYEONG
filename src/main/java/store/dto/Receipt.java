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

    public void updateAdditionalPresentProduct(Product presentProduct, int amount) {
        presentProducts.stream()
                .filter(product -> product.getName().equals(presentProduct.name()))
                .findFirst()
                .ifPresent(product -> product.increaseQuantity());
        updateAdditionalPurchaseProduct(presentProduct, 1, amount);
        updatePromotionDiscount(amount);
    }

    public void updateAdditionalPurchaseProduct(Product purchaseProduct, int quantity, int amount) {
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
        getPurchaseProducts(stringBuilder);
        getPresentProducts(stringBuilder);
        getReceiptResult(stringBuilder);
        return stringBuilder;
    }

    private void getReceiptResult(StringBuilder stringBuilder) {
        balanceAmount = totalAmount - promotionDiscount - membershipDiscount;
        stringBuilder.append("====================================\n");
        stringBuilder.append(String.format("%-17s %-3d %,10d\n", "총구매액", totalCount, totalAmount));
        stringBuilder.append(String.format("%-17s %8s %,d\n", "행사할인", "-", promotionDiscount));
        stringBuilder.append(String.format("%-17s %8s %,d\n", "멤버십할인", "-", membershipDiscount));
        stringBuilder.append(String.format("%-17s %8s %,d\n", "내실돈", "", balanceAmount));
    }

    private void getPresentProducts(StringBuilder stringBuilder) {
        stringBuilder.append("=============증    정===============\n");
        for (PresentProduct presentProduct : presentProducts) {
            stringBuilder.append(String.format("%-19s %-3d\n", presentProduct.getName(), presentProduct.getQuantity()));
        }
    }

    private void getPurchaseProducts(StringBuilder stringBuilder) {
        stringBuilder.append("==============W 편의점================\n");
        stringBuilder.append(String.format("%-17s %-3s %10s\n", "상품명", "수량", "금액"));
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            totalCount += purchaseProduct.getQuantity();
            stringBuilder.append(
                    String.format("%-19s %-3d %,10d\n", purchaseProduct.getName(), purchaseProduct.getQuantity(),
                            purchaseProduct.getAmount()));
        }
    }
}

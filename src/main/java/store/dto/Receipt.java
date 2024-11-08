package store.dto;

import java.util.ArrayList;
import java.util.List;
import store.domain.Product;

public class Receipt{
    private List<PurchaseProduct> purchaseProducts;
    private List<PresentProduct> presentProducts;
    private int totalCount;
    private int totalAmount;
    private int promotionDiscount;
    private int membershipDiscount;
    private int balanceAmount;

    public Receipt(){
        purchaseProducts = new ArrayList<>();
        presentProducts = new ArrayList<>();
    }

    public void updatePurchaseProduct(PurchaseProduct purchaseProduct){
        purchaseProducts.add(purchaseProduct);
    }

    public void updatePresentProducts(PresentProduct presentProduct){
        presentProducts.add(presentProduct);
    }

    public void updateAdditionalPresentProduct(PresentProduct presentProduct){
        presentProducts.stream()
                .filter(product -> product.getName().equals(presentProduct.getName()))
                .findFirst()
                .ifPresent(product -> product.increaseQuantity());
    }
}

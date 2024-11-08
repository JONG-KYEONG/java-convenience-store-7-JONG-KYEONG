package store.dto;

public class PurchaseProduct {
    private String name;
    private int quantity;
    private int amount;
    public PurchaseProduct(String name, int quantity, int amount){
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
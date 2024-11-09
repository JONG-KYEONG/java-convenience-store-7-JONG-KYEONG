package store.dto;

public class PurchaseProduct {
    private String name;
    private int quantity;
    private int amount;

    public int getQuantity() {
        return quantity;
    }

    public PurchaseProduct(String name, int quantity, int amount) {
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void updateAmount(int amount){
        this.amount += amount;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}

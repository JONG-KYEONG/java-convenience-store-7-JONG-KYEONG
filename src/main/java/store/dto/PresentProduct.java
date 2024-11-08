package store.dto;

public class PresentProduct {
    private String name;
    private int quantity;

    public PresentProduct(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(){
        quantity++;
    }
}

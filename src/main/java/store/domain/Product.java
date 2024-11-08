package store.domain;

public record Product(
        String name,
        int price,
        int quantity,
        String promotion
) {
    public Product(Product product, int quantity){
        this(product.name, product.price, quantity, product.promotion);
    }
}

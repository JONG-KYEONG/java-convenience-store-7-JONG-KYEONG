package store.domain;

public record Product(
        String name,
        int price,
        int quantity,
        String promotion
) {
}

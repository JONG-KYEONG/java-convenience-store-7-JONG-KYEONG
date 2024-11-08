package store.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Product;
import store.repository.ProductRepository;

public class InputValidator {
    private final ProductRepository productRepository;

    public InputValidator() {
        this.productRepository = ProductRepository.getInstance();
    }

    public List<Product> getBuyProducts(String input) {
        List<Product> products = makeProductsFromString(input);
        validProducts(products);
        return products;
    }

    public boolean isPlusedAdditionalQuantity(String input) {
        if (input.equals("Y")) {
            return true;
        }
        if (input.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
    }

    public boolean isPurchasedWithoutDiscount(String input) {
        if (input.equals("Y")) {
            return true;
        }
        if (input.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
    }

    public boolean isMembershipDiscountApplied(String input) {
        if (input.equals("Y")) {
            return true;
        }
        if (input.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
    }

    public boolean isAdditionalPurchaseConfirmed(String input) {
        if (input.equals("Y")) {
            return true;
        }
        if (input.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
    }

    private void validProducts(List<Product> products) {
        for (Product product : products) {
            productRepository.validateName(product);
            productRepository.validQuantity(product);
        }
    }

    private List<Product> makeProductsFromString(String productValue) {
        try {
            return Arrays.stream(productValue.split(","))
                    .map(productString -> {
                        String[] parts = productString.replace("[", "").replace("]", "").split("-");
                        return new Product(parts[0], 0, Integer.parseInt(parts[1]), null);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}

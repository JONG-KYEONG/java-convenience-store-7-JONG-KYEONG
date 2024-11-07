package store.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Product;
import store.view.InputView;

public class InputValidator {
    private final InputView inputView;

    InputValidator(InputView inputView) {
        this.inputView = new InputView();
    }

    public List<Product> getBuyProducts(String input) {
        while (true) {
            try {
                return makeProductsFromString(inputView.readBuyProduct());
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    public boolean isPlusedAdditionalQuantity(String productName, int additionalQuantity) {
        while (true) {
            try {
                String input = inputView.readAdditionalQuantity(productName, additionalQuantity);
                if (input.equals("Y")) {
                    return true;
                }
                if (input.equals("N")) {
                    return false;
                }
                throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    public boolean isPurchasedWithoutDiscount(String productName, int quantity) {
        while (true) {
            try {
                String input = inputView.readPurchaseWithoutDiscount(productName, quantity);
                if (input.equals("Y")) {
                    return true;
                }
                if (input.equals("N")) {
                    return false;
                }
                throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    public boolean isMembershipDiscountApplied() {
        while (true) {
            try {
                String input = inputView.readMembershipDiscount();
                if (input.equals("Y")) {
                    return true;
                }
                if (input.equals("N")) {
                    return false;
                }
                throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    public boolean isAdditionalPurchaseConfirmed() {
        while (true) {
            try {
                String input = inputView.readAdditionalPurchase();
                if (input.equals("Y")) {
                    return true;
                }
                if (input.equals("N")) {
                    return false;
                }
                throw new IllegalArgumentException("[ERROR] Y/N 로만 입력해 주세요.");
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    private List<Product> makeProductsFromString(String productValue) {
        try {
            return Arrays.stream(productValue.split(", "))
                    .map(productString -> {
                        String[] parts = productString.replace("[", "").replace("]", "").split("-");
                        return new Product(parts[0], 0, Integer.parseInt(parts[1]), null);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 입력 형식을 다시 확인해 주세요.");
        }
    }
}

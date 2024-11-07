package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

import java.util.List;
import store.domain.Product;
import store.validator.InputValidator;

public class InputView {
    private final InputValidator inputValidator;

    public InputView() {
        this.inputValidator = new InputValidator();
    }

    public List<Product> readBuyProduct() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        while (true) {
            try {
                List<Product> products = inputValidator.getBuyProducts(readLine());
                return products;
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    public boolean readAdditionalQuantity(String productName, int additionalQuantity) {
        System.out.println("현재 " + productName + "은(는) " + additionalQuantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        while (true) {
            try {
                return inputValidator.isPlusedAdditionalQuantity(readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }


    public boolean readPurchaseWithoutDiscount(String productName, int quantity) {
        System.out.println("현재 " + productName + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        while (true) {
            try {
                return inputValidator.isPurchasedWithoutDiscount(readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }


    public boolean readMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        while (true) {
            try {
                return inputValidator.isMembershipDiscountApplied(readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }

    public boolean readAdditionalPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        while (true) {
            try {
                return inputValidator.isAdditionalPurchaseConfirmed(readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }
}

package store.view;

import java.util.List;
import store.domain.Product;

public class OutputView {
    public void printLn() {
        System.out.println();
    }

    public void printWelcomeAndProductInfo() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
    }

    public void printProductInfo(List<Product> stackProducts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : stackProducts) {
            String price = getPrice(product);
            String quantity = getQuantity(product);
            String promotion = getPromotion(product);

            stringBuilder.append("- " + product.name() + " " + price + quantity + promotion + "\n");
        }
        System.out.println(stringBuilder);
    }

    private static String getPrice(Product product) {
        String price = String.format("%,d", product.price()) + "원 ";
        return price;
    }

    private static String getPromotion(Product product) {
        String promotion = product.promotion();
        if (promotion.equals("null")) {
            promotion = "";
        }
        return promotion;
    }

    private static String getQuantity(Product product) {
        String quantity = product.quantity() + "개 ";
        if (product.quantity() == 0) {
            quantity = "재고 없음 ";
        }
        return quantity;
    }

    public void printReceipt(StringBuilder stringBuilder) {
        System.out.println(stringBuilder);
    }
}

package store.view;

import java.util.List;
import store.domain.Product;

public class OutputView {
    public void printLn(){
        System.out.println();
    }
    public void printWelcomeAndProductInfo() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
    }

    public void printProductInfo(List<Product> stackProducts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : stackProducts) {
            String price = String.format("%,d", product.price()) + "원 ";
            String quantity = product.quantity() + "개 ";
            String promotion = product.promotion();
            if (product.quantity() == 0) {
                quantity = "재고 없음 ";
            }
            if(promotion.equals("null")){
                promotion = "";
            }

            stringBuilder.append("- " + product.name() + " " + price + quantity + promotion + "\n");
        }
        System.out.println(stringBuilder);
    }

    public void printReceipt(StringBuilder stringBuilder){
        System.out.println(stringBuilder);
    }
}

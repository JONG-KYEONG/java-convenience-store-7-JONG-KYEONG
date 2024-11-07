package store.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import store.domain.Product;
import store.view.InputView;

public class InputValidator {
     private final InputView inputView;
     InputValidator(InputView inputView){
         this.inputView = new InputView();
     }

     public List<Product> getBuyProducts(){
         while (true) {
             try {
                 return makeProductsFromString(inputView.readBuyProduct());
             } catch (Exception e) {
                 throw new IllegalArgumentException("[ERROR] 입력 형식을 다시 확인해 주세요");
             }
         }
    }

    private List<Product> makeProductsFromString(String productValue){
         return Arrays.stream(productValue.split(", "))
                 .map(productString -> {
                     String[] parts = productString.replace("[", "").replace("]", "").split("-");
                     String name = parts[0];
                     int quantity = Integer.parseInt(parts[1]);
                     return new Product(name, 0, quantity, null);
                 })
                 .collect(Collectors.toList());
    }
}

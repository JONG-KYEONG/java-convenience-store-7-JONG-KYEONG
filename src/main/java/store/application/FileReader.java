package store.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.constants.Constants;
import store.domain.Product;

public class FileReader {

    public List<Product> getProducts(){
        List<String> inputProducts = readProductsFile();
        List<Product> products = new ArrayList<>();
        for(String product : inputProducts){
            String [] productValue = product.split(",");
            products.add(new Product(productValue[0], Integer.parseInt(productValue[1]), Integer.parseInt(productValue[2]), productValue[3]));
        }
        return products;
    }
    public List<String> readProductsFile(){
        List<String> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(Constants.PRODUCT_RESOURCE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                products.add(line);
            }
            return products;
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] products.md 파일의 경로를 다시 확인해 주세요");
        }
    }
}

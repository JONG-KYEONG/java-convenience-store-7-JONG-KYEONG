package store.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.constants.Constants;
import store.domain.Product;
import store.domain.Promotion;

public class FileReader {

    public List<Product> getProducts() {
        List<String> inputProducts = readProductsFile();
        List<Product> products = new ArrayList<>();
        for (String product : inputProducts) {
            String[] productValue = product.split(",");
            if (productValue[0].equals("name")) {
                continue;
            }
            products.add(
                    new Product(productValue[0], Integer.parseInt(productValue[1]), Integer.parseInt(productValue[2]),
                            productValue[3]));
        }
        return products;
    }

    public List<String> readProductsFile() {
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

    public List<Promotion> getPromotions() {
        List<String> inputPromotions = readPromotionsFile();
        List<Promotion> promotions = new ArrayList<>();
        for (String promotion : inputPromotions) {
            String[] promotionValue = promotion.split(",");
            if (promotionValue[0].equals("name")) {
                continue;
            }
            promotions.add(new Promotion(promotionValue[0], Integer.parseInt(promotionValue[1]),
                    Integer.parseInt(promotionValue[2]),
                    LocalDate.parse(promotionValue[3]), LocalDate.parse(promotionValue[4])));
        }
        return promotions;
    }

    public List<String> readPromotionsFile() {
        List<String> promotions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(Constants.PROMOTION_RESOURCE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                promotions.add(line);
            }
            return promotions;
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] promotions.md 파일의 경로를 다시 확인해 주세요");
        }
    }
}

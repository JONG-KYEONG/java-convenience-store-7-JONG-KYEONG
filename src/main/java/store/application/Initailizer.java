package store.application;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

public class Initailizer {
    public void initProductRepository(List<Product> products){
        ProductRepository.init(products);
    }

    public void initPromotionRepository(List<Promotion> promotions){
        PromotionRepository.init(promotions);
    }
}

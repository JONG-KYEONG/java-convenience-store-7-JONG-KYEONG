package store.repository;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;

public class PromotionRepository {
    private static PromotionRepository instance;
    private final List<Promotion> promotions;

    private PromotionRepository(List<Promotion> promotions){
        this.promotions = promotions;
    }

    public static PromotionRepository getInstance() {
        return instance;
    }

    public static void init(List<Promotion> promotions) {
        if (instance == null) {
            instance = new PromotionRepository(promotions);
        }
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }
}

package store.repository;

import static camp.nextstep.edu.missionutils.DateTimes.now;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import store.domain.Promotion;

public class PromotionRepository {
    private static PromotionRepository instance;
    private final List<Promotion> promotions;

    private PromotionRepository(List<Promotion> promotions) {
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

    public Optional<Promotion> findValidPromotionByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.name().equals(name) && validPromotion(promotion))
                .findFirst();
    }

    private boolean validPromotion(Promotion promotion) {
        LocalDate today = LocalDate.from(now());
        if (today.isAfter(promotion.start_date()) && today.isBefore(promotion.end_date())) {
            return true;
        }
        if (today.isEqual(promotion.start_date()) || today.isEqual(promotion.end_date())) {
            return true;
        }
        return false;
    }
}

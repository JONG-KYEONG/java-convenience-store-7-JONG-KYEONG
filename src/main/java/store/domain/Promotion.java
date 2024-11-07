package store.domain;

import java.time.LocalDate;

public record Promotion(
        String name,
        int buy,
        int get,
        LocalDate start_date,
        LocalDate end_date
) {
}

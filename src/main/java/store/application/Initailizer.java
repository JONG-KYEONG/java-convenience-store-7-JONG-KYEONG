package store.application;

import java.util.List;
import store.domain.Product;
import store.repository.ProductRepository;

public class Initailizer {
    private final FileReader fileReader;

    public Initailizer(){
        this.fileReader = new FileReader();
    }

    public void initProductRepository(List<Product> products){
        ProductRepository.init(products);
    }
}

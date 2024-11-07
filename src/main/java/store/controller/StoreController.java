package store.controller;

import java.util.List;
import store.application.FileReader;
import store.application.Initailizer;
import store.application.PromotionService;
import store.domain.Product;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final Initailizer initailizer;
    private final FileReader fileReader;
    private final PromotionService promotionService;
    private final InputView inputView;
    private final OutputView outputView;
    public StoreController(){
        this.initailizer = new Initailizer();
        this.fileReader = new FileReader();
        initRepository();
        this.promotionService = new PromotionService();
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    private void initRepository(){
        initailizer.initProductRepository(fileReader.getProducts());
        initailizer.initPromotionRepository(fileReader.getPromotions());
    }

    public void run(){
        List<Product> purchaseProducts = inputView.readBuyProduct();
        List<Product> giftEligibleProducts = checkPromotion(purchaseProducts);
    }

    public List<Product> checkPromotion(List<Product> products){
        List<Product> giftEligibleProducts = promotionService.getGiftEligibleProducts(products);
        for (Product product : giftEligibleProducts){
            if(!inputView.readAdditionalQuantity(product.name())){
                giftEligibleProducts.remove(product);
            }
        }
        return giftEligibleProducts;
    }

}

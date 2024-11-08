package store.controller;

import java.util.List;
import store.application.FileReader;
import store.application.Initailizer;
import store.application.ProductService;
import store.application.PromotionService;
import store.domain.Product;
import store.dto.Receipt;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final Initailizer initailizer;
    private final FileReader fileReader;
    private final PromotionService promotionService;
    private final ProductService productService;
    private final InputView inputView;
    private final OutputView outputView;
    public StoreController(){
        this.initailizer = new Initailizer();
        this.fileReader = new FileReader();
        initRepository();
        this.promotionService = new PromotionService();
        this.productService = new ProductService();
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    private void initRepository(){
        initailizer.initProductRepository(fileReader.getProducts());
        initailizer.initPromotionRepository(fileReader.getPromotions());
    }

    public void run(){
        List<Product> inputProducts = inputView.readBuyProduct();
        List<Product> promotionProduct = promotionService.getPromotionProduct(inputProducts);
        List<Product> giftEligibleProducts = checkPromotion(promotionProduct);

    }

    public Receipt updateReceipt(List<Product> purchaseProducts,List<Product> promotionProduct ,List<Product> giftEligibleProducts){
        Receipt receipt = new Receipt();
        receipt = promotionService.calculatePromotionPrice(receipt, promotionProduct);
        receipt = promotionService.updateAdditionalPromotion(receipt, giftEligibleProducts);
        return receipt;
    }

    public List<Product> checkPromotion(List<Product> promotionProduct){
        List<Product> giftEligibleProducts = promotionService.getGiftEligibleProducts(promotionProduct);
        for (Product product : giftEligibleProducts){
            if(!inputView.readAdditionalQuantity(product.name())){
                giftEligibleProducts.remove(product);
            }
        }
        return giftEligibleProducts;
    }

}

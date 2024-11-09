package store.controller;

import java.util.ArrayList;
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

    public StoreController() {
        this.initailizer = new Initailizer();
        this.fileReader = new FileReader();
        initRepository();
        this.promotionService = new PromotionService();
        this.productService = new ProductService();
        this.inputView = new InputView();
        this.outputView = new OutputView();
    }

    private void initRepository() {
        initailizer.initProductRepository(fileReader.getProducts());
        initailizer.initPromotionRepository(fileReader.getPromotions());
    }

    public void runProcess() {
        outputView.printWelcomeAndProductInfo();
        outputView.printProductInfo(productService.getStackProduct());

        List<Product> inputProducts = inputView.readBuyProduct();
        List<Product> promotionProduct = promotionService.getPromotionProduct(inputProducts);
        List<Product> giftEligibleProducts = checkAdditionalGiftEligibility(promotionProduct); // 추가 증정품 리스트
        List<Product> purchaseProducts = productService.getPurchaseProduct(inputProducts,
                promotionProduct); // 프로모션 없이 구매하는 상품 리스트
        List<Product> purchaseWithPromotionProducts = checkAdditionalProductWithoutPromotion(inputProducts,
                promotionProduct);  // 프로모션 재고 소진으로 프로모션 없이 구매하는 상품 리스트
        Boolean hasMembership = inputView.readMembershipDiscount();
        Receipt receipt = new Receipt();
        receipt = updateReceiptWithPromotion(receipt, promotionProduct, giftEligibleProducts);
        receipt = updateReceipt(receipt,purchaseProducts ,hasMembership);
        receipt = updateReceiptAdditionalPurchase(receipt,purchaseWithPromotionProducts ,hasMembership);
        outputView.printReceipt(receipt.toStringBuilder());
    }

    public void run(){
        do {
            runProcess();
        } while (inputView.readAdditionalPurchase());
    }

    public Receipt updateReceipt(Receipt receipt, List<Product> purchaseProducts,boolean hasMembership) { // 일반 상품들 구매, 영수증 갱신
        return productService.calculatePrice(receipt, purchaseProducts, hasMembership);
    }

    public Receipt updateReceiptAdditionalPurchase(Receipt receipt, List<Product> purchaseWithPromotionProducts,boolean hasMembership) { // 일반 상품들 구매, 영수증 갱신
        return productService.calculatePriceAdditionalProduct(receipt, purchaseWithPromotionProducts, hasMembership);
    }


    public Receipt updateReceiptWithPromotion(Receipt receipt, List<Product> promotionProduct,
                                              List<Product> giftEligibleProducts) { // 프로모션 상품들 구매, 영수증 갱신
        receipt = promotionService.calculatePromotionPrice(receipt, promotionProduct);
        receipt = promotionService.updateAdditionalPromotion(receipt, giftEligibleProducts);
        return receipt;
    }

    public List<Product> checkAdditionalGiftEligibility(List<Product> promotionProduct) {  // 추가 증정품 받을 건지 확인하는 컨트롤러
        List<Product> giftEligibleProducts = promotionService.getGiftEligibleProducts(promotionProduct);
        List<Product> newGiftEligibleProducts = new ArrayList<>();
        for (Product product : giftEligibleProducts) {
            if (inputView.readAdditionalQuantity(product.name())) {
                newGiftEligibleProducts.add(product);
            }
        }
        return newGiftEligibleProducts;
    }

    public List<Product> checkAdditionalProductWithoutPromotion(List<Product> inputProducts,
                                                                List<Product> promotionProducts) {  // 프로모션 재고 없어서 없는 상품들 프로모션 없이 구매할 건지 확인하는 컴트롤러
        List<Product> additionalProductsWithoutPromotion = productService.getPurchaseProductWithoutPromotion(
                inputProducts, promotionProducts);
        for (Product product : additionalProductsWithoutPromotion) {
            if (!inputView.readPurchaseWithoutDiscount(product.name(), product.quantity())) {
                additionalProductsWithoutPromotion.remove(product);
            }
        }
        return additionalProductsWithoutPromotion;
    }
}

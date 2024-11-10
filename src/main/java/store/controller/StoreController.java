package store.controller;

import java.util.ArrayList;
import java.util.List;
import store.application.FileReader;
import store.application.Initailizer;
import store.application.ProductService;
import store.application.PromotionService;
import store.domain.Product;
import store.dto.ProductProcessingResults;
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

    public void initRepository() {
        initailizer.initProductRepository(fileReader.getProducts());
        initailizer.initPromotionRepository(fileReader.getPromotions());
    }

    public void runProcess() {
        outputView.printWelcomeAndProductInfo();
        outputView.printProductInfo(productService.getStackProduct());

        List<Product> inputProducts = inputView.readBuyProduct();
        ProductProcessingResults productProcessingResults = processProducts(inputProducts);

        boolean hasMembership = inputView.readMembershipDiscount();
        Receipt receipt = createReceiptWithPromotionAndPurchase(productProcessingResults, hasMembership);

        outputView.printReceipt(receipt.toStringBuilder());
    }

    public void run() {
        runProcess();
        while (inputView.readAdditionalPurchase()) {
            outputView.printLn();
            runProcess();
        }
    }

    public Receipt updateReceipt(Receipt receipt, List<Product> purchaseProducts,
                                 boolean hasMembership) { // 일반 상품들 구매, 영수증 갱신
        return productService.calculatePrice(receipt, purchaseProducts, hasMembership);
    }

    public Receipt updateReceiptAdditionalPurchase(Receipt receipt, List<Product> purchaseWithPromotionProducts,
                                                   boolean hasMembership) { // 일반 상품들 구매, 영수증 갱신
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
        List<Product> newAdditionalProductsWithoutPromotion = new ArrayList<>();
        for (Product product : additionalProductsWithoutPromotion) {
            if (inputView.readPurchaseWithoutDiscount(product.name(), product.quantity())) {
                newAdditionalProductsWithoutPromotion.add(product);
            }
        }
        return newAdditionalProductsWithoutPromotion;
    }

    private ProductProcessingResults processProducts(List<Product> inputProducts) {  // 로직에 필요한 리스트 생성
        List<Product> promotionProduct = promotionService.getPromotionProduct(inputProducts);
        List<Product> giftEligibleProducts = checkAdditionalGiftEligibility(promotionProduct);
        List<Product> purchaseProducts = productService.getPurchaseProduct(inputProducts, promotionProduct);
        List<Product> purchaseWithPromotionProducts = checkAdditionalProductWithoutPromotion(inputProducts,
                promotionProduct);

        return new ProductProcessingResults(promotionProduct, giftEligibleProducts, purchaseProducts,
                purchaseWithPromotionProducts);
    }

    private Receipt createReceiptWithPromotionAndPurchase(ProductProcessingResults productProcessingResults,
                                                          boolean hasMembership) {  // 영수증 업데이트
        Receipt receipt = new Receipt();
        receipt = updateReceiptWithPromotion(receipt, productProcessingResults.getPromotionProduct(),
                productProcessingResults.getGiftEligibleProducts());
        receipt = updateReceipt(receipt, productProcessingResults.getPurchaseProducts(), hasMembership);
        receipt = updateReceiptAdditionalPurchase(receipt, productProcessingResults.getPurchaseWithPromotionProducts(),
                hasMembership);
        return receipt;
    }
}

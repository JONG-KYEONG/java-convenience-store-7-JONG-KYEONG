package store.controller;

import store.application.FileReader;
import store.application.Initailizer;

public class StoreController {
    private final Initailizer initailizer;
    private final FileReader fileReader;
    public StoreController(){
        this.initailizer = new Initailizer();
        this.fileReader = new FileReader();
        initRepository();
    }

    private void initRepository(){
        initailizer.initProductRepository(fileReader.getProducts());
        initailizer.initPromotionRepository(fileReader.getPromotions());
    }
}

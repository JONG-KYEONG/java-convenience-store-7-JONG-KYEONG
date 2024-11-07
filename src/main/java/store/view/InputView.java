package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class InputView {
    public String readBuyProduct(){
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return readLine();
    }

    public String readAdditionalQuantity(String productName, int AdditionalQuantity){
        System.out.println("현재 "+productName+"은(는) "+AdditionalQuantity+"개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        return readLine();
    }
}

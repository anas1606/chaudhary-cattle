package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import org.springframework.stereotype.Controller;

@Controller
public class FoodUsage {

    public void loadMilk(){
        new CommanUtils().loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadMadicine(){
        new CommanUtils().loadPage(FxmlPaths.FARM_MADICINE);
    }
    public void loadFoodPurchase(){
        new CommanUtils().loadPage(FxmlPaths.FARM_FOOD_PURCHASE);
    }
}

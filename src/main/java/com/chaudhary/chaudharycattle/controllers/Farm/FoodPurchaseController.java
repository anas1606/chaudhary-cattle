package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import org.springframework.stereotype.Controller;

@Controller
public class FoodPurchaseController {

    public void loadMilk(){
        CommanUtils.loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadFoodUsage(){
        CommanUtils.loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadMadicine(){
        CommanUtils.loadPage(FxmlPaths.FARM_MADICINE);
    }
}

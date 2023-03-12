package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import org.springframework.stereotype.Controller;

@Controller
public class FoodPurchase {

    public void loadMilk(){
        new CommanUtils().loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadFoodUsage(){
        new CommanUtils().loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadMadicine(){
        new CommanUtils().loadPage(FxmlPaths.FARM_MADICINE);
    }
}

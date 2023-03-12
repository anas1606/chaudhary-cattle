package com.chaudhary.chaudharycattle.controllers.Farm;

import com.chaudhary.chaudharycattle.utils.CommanUtils;
import com.chaudhary.chaudharycattle.utils.FxmlPaths;
import org.springframework.stereotype.Controller;

@Controller
public class Madicine {

    public void loadMilk(){
        new CommanUtils().loadPage(FxmlPaths.FARM_MILK);
    }
    public void loadFoodUsage(){
        new CommanUtils().loadPage(FxmlPaths.FARM_FOOD_USAGE);
    }
    public void loadFoodPurchase(){
        new CommanUtils().loadPage(FxmlPaths.FARM_FOOD_PURCHASE);
    }

}

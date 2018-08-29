package org.parabot.scriptwriter.revival.aGemCutter.strategies;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.scriptwriter.revival.aGemCutter.core.Core;
import org.parabot.scriptwriter.revival.aGemCutter.revival_lib.Constants;
import org.rev317.min.api.methods.*;

public class Cutting implements Strategy {
    @Override
    public boolean activate() {
        return Inventory.getCount(Core.getSettings().getUncutGemId()) > 0
                && Players.getMyPlayer().getAnimation() != Constants.CUTTING_ANIMATION_ID;
    }

    @Override
    public void execute() {
        Inventory.getItem(Constants.CHISEL_ID).interact(Items.Option.USE);
        Time.sleep(500);
        Inventory.getItem(Core.getSettings().getUncutGemId()).interact(Items.Option.USE_WITH);
        Time.sleep(new SleepCondition() {
            @Override
            public boolean isValid() {
                return Interfaces.getBackDialogId() == Constants.CUT_GEM_DIALOG_ID;
            }
        }, 2000);
        if(Interfaces.getBackDialogId() == Constants.CUT_GEM_DIALOG_ID) {
            Menu.clickButton(Constants.CUT_ALL);
            Time.sleep(new SleepCondition() {
                @Override
                public boolean isValid() {
                    return Players.getMyPlayer().getAnimation() == Constants.CUTTING_ANIMATION_ID;
                }
            }, 2000);
        }
    }
}

package org.parabot.scriptwriter.revival.aGemCutter.strategies;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.scriptwriter.revival.aGemCutter.core.Core;
import org.parabot.scriptwriter.revival.aGemCutter.revival_lib.Constants;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

public class Banking implements Strategy {
    @Override
    public boolean activate() {
        return Inventory.getCount(Core.getSettings().getUncutGemId()) == 0;
    }

    @Override
    public void execute() {
        if(Bank.isOpen()) {
            if(Bank.getCount(Core.getSettings().getUncutGemId()) == 0) {
                Logger.addMessage("-------- Out of uncut gems, stopping script --------");
                Core.stopScript();
                return;
            }
            if(Inventory.getCount(Core.getSettings().getUncutGemId()) == 0) {
                Bank.depositAllExcept(Constants.CHISEL_ID);
                Time.sleep(new SleepCondition() {
                    @Override
                    public boolean isValid() {
                        return inventoryEmptyExcept(new int[]{Constants.CHISEL_ID});
                    }
                },2000);
            }
            if(inventoryEmptyExcept(new int[]{Constants.CHISEL_ID})) {
                Menu.clickButton(5387);
                Time.sleep(1000);
                Bank.withdraw(Core.getSettings().getUncutGemId(), 27, 1000);
                Time.sleep(new SleepCondition() {
                    @Override
                    public boolean isValid() {
                        return Inventory.getCount(Core.getSettings().getUncutGemId()) == 27;
                    }
                }, 3000);
                if(Inventory.getCount(Core.getSettings().getUncutGemId()) == 27) {
                    Bank.close();
                }
            }
        } else {
            SceneObject bank = SceneObjects.getClosest(Constants.BANK_ID);
            Bank.open(bank);
            Time.sleep(new SleepCondition() {
                @Override
                public boolean isValid() {
                    return Bank.isOpen();
                }
            }, 4000);
        }
    }

    private boolean inventoryEmptyExcept(int[] ids) {
        if(Inventory.getCount() != ids.length) return false;
        for(int i : ids) {
            if(Inventory.getCount(i) != 1) return false;
        }
        return true;
    }

}

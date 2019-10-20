package org.parabot.scriptwriter.revival.aGemCutter.core;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.scriptwriter.revival.aGemCutter.UI.GUI;
import org.parabot.scriptwriter.revival.aGemCutter.data.Settings;
import org.parabot.scriptwriter.revival.aGemCutter.strategies.Banking;
import org.parabot.scriptwriter.revival.aGemCutter.strategies.Cutting;

import java.util.ArrayList;

@ScriptManifest(author = "Atex",
        category = Category.CRAFTING,
        description = "Cuts gems of choice",
        name = "aGemCutter", servers = { "Revival" },
        version = 0.1)
public class Core extends Script {
    private ArrayList<Strategy> strategies = new ArrayList<>();
    private static Settings settings;
    private static Core core;

    @Override
    public boolean onExecute() {
        strategies.add(new Banking());
        strategies.add(new Cutting());
            
        provide(strategies);
            
        core = this;

        GUI gui = new GUI();
        while(gui.isVisible()) { //Wait until GUI closes, meaning user has entered settings
            Time.sleep(100);
        }

        if(gui.getSettings() == null) {
            Logger.addMessage("Invalid input, stopping script");
            return false;
        }

        settings = gui.getSettings();
            
        if(settings == null) return false;

        return true;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void stopScript() {
        core.setState(STATE_STOPPED);
    }
}

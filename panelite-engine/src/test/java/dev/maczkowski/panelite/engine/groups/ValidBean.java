package dev.maczkowski.panelite.engine.groups;

import dev.maczkowski.panelite.engine.api.PaneliteGroup;
import dev.maczkowski.panelite.engine.api.PaneliteMethod;
import dev.maczkowski.panelite.engine.api.PaneliteMethodType;

@PaneliteGroup(name = "validGroup")
public class ValidBean {

    @PaneliteMethod(name = "validCommand", type = PaneliteMethodType.COMMAND)
    public void validCommand(Object param) {
    }

    @PaneliteMethod(name = "validQuery", type = PaneliteMethodType.QUERY)
    public boolean validQuery() {
        return true;
    }
}

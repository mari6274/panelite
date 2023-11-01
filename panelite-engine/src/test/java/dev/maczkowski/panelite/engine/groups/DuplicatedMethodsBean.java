package dev.maczkowski.panelite.engine.groups;

import dev.maczkowski.panelite.engine.api.PaneliteGroup;
import dev.maczkowski.panelite.engine.api.PaneliteMethod;
import dev.maczkowski.panelite.engine.api.PaneliteMethodType;

@PaneliteGroup(name = "duplicatedMethodsGroup")
public class DuplicatedMethodsBean {

    @PaneliteMethod(name = "duplicatedCommand", type = PaneliteMethodType.COMMAND)
    public void duplicatedCommand1(Object param) {
    }

    @PaneliteMethod(name = "duplicatedCommand", type = PaneliteMethodType.COMMAND)
    public void duplicatedCommand2(Object param) {
    }

    @PaneliteMethod(name = "duplicatedQuery", type = PaneliteMethodType.QUERY)
    public boolean duplicatedQuery1() {
        return true;
    }

    @PaneliteMethod(name = "duplicatedQuery", type = PaneliteMethodType.QUERY)
    public boolean duplicatedQuery2() {
        return true;
    }

}

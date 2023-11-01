package dev.maczkowski.panelite.example;

import dev.maczkowski.panelite.engine.api.PaneliteParam;
import dev.maczkowski.panelite.engine.api.PaneliteParamType;

public record ExampleData(@PaneliteParam(name = "uid", type = PaneliteParamType.TEXT) String uid,
                          @PaneliteParam(name = "param", type = PaneliteParamType.COMPLEX)  ExampleParam param) {
}

package dev.maczkowski.panelite.example;

import dev.maczkowski.panelite.engine.api.PaneliteParam;
import dev.maczkowski.panelite.engine.api.PaneliteParamType;

public record ExampleParam(@PaneliteParam(name = "key", type = PaneliteParamType.TEXT) String key,
                           @PaneliteParam(name = "value", type = PaneliteParamType.TEXT) String value) {
}

package dev.maczkowski.panelite.engine.dto;

import dev.maczkowski.panelite.engine.api.PaneliteMethodType;
import lombok.Value;

import java.util.List;

@Value
public class PaneliteMethodDto {
    String name;
    PaneliteMethodType type;
    List<PaneliteParamDto> params;

}

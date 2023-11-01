package dev.maczkowski.panelite.engine.dto;

import lombok.Value;

import java.util.List;

@Value
public class PaneliteGroupDto {
    String name;
    List<PaneliteMethodDto> methods;
}

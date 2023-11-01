package dev.maczkowski.panelite.engine.dto;

import dev.maczkowski.panelite.engine.api.PaneliteParamType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract sealed class PaneliteParamDto permits SimplePaneliteParamDto, ComplexPaneliteParamDto {
    private final String name;
    public abstract PaneliteParamType getType();
}

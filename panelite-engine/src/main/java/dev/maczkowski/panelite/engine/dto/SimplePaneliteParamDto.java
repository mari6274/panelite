package dev.maczkowski.panelite.engine.dto;

import dev.maczkowski.panelite.engine.api.PaneliteParamType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class SimplePaneliteParamDto extends PaneliteParamDto {
    private final PaneliteParamType type;
    public SimplePaneliteParamDto(String name, PaneliteParamType type) {
        super(name);
        if (type == PaneliteParamType.COMPLEX) {
            throw new IllegalArgumentException("Cannot pass type COMPLEX to simple param");
        }
        this.type = type;
    }
}

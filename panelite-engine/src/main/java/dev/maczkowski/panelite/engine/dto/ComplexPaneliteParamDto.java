package dev.maczkowski.panelite.engine.dto;

import dev.maczkowski.panelite.engine.api.PaneliteParamType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ComplexPaneliteParamDto extends PaneliteParamDto {
    private final List<PaneliteParamDto> params;

    public ComplexPaneliteParamDto(String name, List<PaneliteParamDto> params) {
        super(name);
        this.params = Optional.ofNullable(params).orElseGet(Collections::emptyList);
    }

    public PaneliteParamType getType() {
        return PaneliteParamType.COMPLEX;
    }
}

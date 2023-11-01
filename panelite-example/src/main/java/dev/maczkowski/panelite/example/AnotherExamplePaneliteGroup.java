package dev.maczkowski.panelite.example;

import dev.maczkowski.panelite.engine.api.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@PaneliteGroup(name = "another_group")
public class AnotherExamplePaneliteGroup {

    @PaneliteMethod(name = "say_hello", type = PaneliteMethodType.QUERY)
    public String hello() {
        return "hello world!";
    }

}

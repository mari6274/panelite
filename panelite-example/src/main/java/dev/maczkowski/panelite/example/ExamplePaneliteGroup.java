package dev.maczkowski.panelite.example;

import dev.maczkowski.panelite.engine.api.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@PaneliteGroup(name = "hello")
public class ExamplePaneliteGroup {

    @PaneliteMethod(name = "say_hello", type = PaneliteMethodType.QUERY)
    public String hello() {
        return "hello world!";
    }

    @PaneliteMethod(name = "say_hello2", type = PaneliteMethodType.QUERY)
    public String hello(@PaneliteParam(name = "name", type = PaneliteParamType.TEXT) String name) {
        return "hello " + name;
    }

    @PaneliteMethod(name = "say_hello3", type = PaneliteMethodType.QUERY)
    public String hello(@PaneliteParam(name = "name", type = PaneliteParamType.TEXT) String name,
                        @PaneliteParam(name = "integer", type = PaneliteParamType.INTEGER) long integer) {
        return "hello " + name + " integer: " + integer;
    }

    @PaneliteMethod(name = "say_hello6", type = PaneliteMethodType.QUERY)
    public String hello(@PaneliteParam(name = "name", type = PaneliteParamType.TEXT) String name,
                        @PaneliteParam(name = "decimal", type = PaneliteParamType.DECIMAL) BigDecimal decimal) {
        return "hello " + name + " decimal: " + decimal;
    }

    @PaneliteMethod(name = "say_hello4", type = PaneliteMethodType.QUERY)
    public String hello(@PaneliteParam(name = "number", type = PaneliteParamType.DECIMAL) BigDecimal number) {
        return "hello number: " + number;
    }

    @PaneliteMethod(name = "say_hello5", type = PaneliteMethodType.QUERY)
    public String hello(@PaneliteParam(name = "boolean", type = PaneliteParamType.BOOLEAN) boolean bool) {
        return "hello boolean: " + bool;
    }

    @PaneliteMethod(name = "post_data", type = PaneliteMethodType.COMMAND)
    public void postData(@PaneliteParam(name = "data", type = PaneliteParamType.COMPLEX) ExampleData data) {
    }

    @PaneliteMethod(name = "long_hello", type = PaneliteMethodType.QUERY)
    public String longProcessingHello() throws InterruptedException {
        Thread.sleep(5000);
        return "hello world!";
    }

}

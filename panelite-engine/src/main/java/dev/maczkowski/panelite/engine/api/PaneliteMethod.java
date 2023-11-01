package dev.maczkowski.panelite.engine.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PaneliteMethod {

    /**
     * Logical name for panelite method. It should match following pattern "^[a-zA-z0-9_]+$",
     * otherwise method won't be recognized.
     */
    String name();

    /**
     * Specifies if method is going to be invoked using:
     * POST ({@link PaneliteMethodType#COMMAND}) or
     * GET ({@link PaneliteMethodType#QUERY}) HTTP method
     */
    PaneliteMethodType type();
}

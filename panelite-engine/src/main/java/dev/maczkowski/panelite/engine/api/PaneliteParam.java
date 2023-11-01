package dev.maczkowski.panelite.engine.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
public @interface PaneliteParam {

    /**
     * Logical name for panelite param. It should match following pattern "^[a-zA-z_][a-zA-z0-9_]*$",
     * otherwise param won't be recognized.
     */
    String name();

    String description() default "";

    PaneliteParamType type();


}

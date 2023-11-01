package dev.maczkowski.panelite.engine.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PaneliteGroup {

    /**
     * Logical name for panelite group of methods. It should match following pattern "^[a-zA-z0-9_]+$",
     * otherwise group won't be recognized.
     */
    String name();
}

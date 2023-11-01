package dev.maczkowski.panelite.engine;

import dev.maczkowski.panelite.engine.api.PaneliteGroup;
import dev.maczkowski.panelite.engine.api.PaneliteMethod;
import dev.maczkowski.panelite.engine.api.PaneliteMethodType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class PaneliteExecutor {

    private final ApplicationContext applicationContext;

    public Optional<ObjectMethod> findCommandObjectMethod(String group, String method) {
        return findBean(group).flatMap(o -> findCommandMethod(o, method)
                .map(m -> new ObjectMethod(o, m)));
    }

    public Optional<ObjectMethod> findQueryObjectMethod(String group, String method) {
        return findBean(group).flatMap(o -> findQueryMethod(o, method)
                .map(m -> new ObjectMethod(o, m)));
    }

    private Optional<Object> findBean(String group) {
        return applicationContext.getBeansWithAnnotation(PaneliteGroup.class).values().stream()
                .filter(o -> o.getClass().getAnnotation(PaneliteGroup.class).name().equals(group))
                .reduce((o1, o2) -> {
                    throw new IllegalStateException("Multiple PaneliteGroup beans with the same name");
                });
    }

    private Optional<Method> findCommandMethod(Object object, String method) {
        return Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(m -> m.getAnnotation(PaneliteMethod.class) != null)
                .filter(m -> m.getAnnotation(PaneliteMethod.class).name().equals(method))
                .filter(m -> m.getAnnotation(PaneliteMethod.class).type().equals(PaneliteMethodType.COMMAND))
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> void.class.equals(m.getReturnType()))
                .reduce((m1, m2) -> {
                    throw new IllegalStateException("Multiple PaneliteMethod methods with the same name");
                });
    }

    private Optional<Method> findQueryMethod(Object object, String method) {
        return Arrays.stream(object.getClass().getMethods())
                .filter(m -> m.getAnnotation(PaneliteMethod.class) != null)
                .filter(m -> m.getAnnotation(PaneliteMethod.class).name().equals(method))
                .filter(m -> m.getAnnotation(PaneliteMethod.class).type().equals(PaneliteMethodType.QUERY))
                .filter(m -> !void.class.equals(m.getReturnType()))
                .reduce((m1, m2) -> {
                    throw new IllegalStateException("Multiple PaneliteMethod methods with the same name");
                });
    }
}

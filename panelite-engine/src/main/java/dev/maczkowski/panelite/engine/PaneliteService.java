package dev.maczkowski.panelite.engine;

import dev.maczkowski.panelite.engine.api.*;
import dev.maczkowski.panelite.engine.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PaneliteService {

    private final ApplicationContext applicationContext;

    public List<PaneliteGroupDto> findAvailableOperations() {
        return applicationContext.getBeansWithAnnotation(PaneliteGroup.class).values().stream()
                .map(o -> {
                    PaneliteGroup annotation = o.getClass().getAnnotation(PaneliteGroup.class);
                    List<PaneliteMethodDto> methods = findAvailableMethods(o);
                    return new PaneliteGroupDto(annotation.name(), methods);
                })
                .toList();
    }

    private List<PaneliteMethodDto> findAvailableMethods(Object o) {
        return Arrays.stream(o.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PaneliteMethod.class))
                .map(method -> {
                    PaneliteMethod annotation = method.getAnnotation(PaneliteMethod.class);
                    List<PaneliteParamDto> params = findAvailableParams(method);
                    return new PaneliteMethodDto(annotation.name(), annotation.type(), params);
                })
                .toList();
    }

    private List<PaneliteParamDto> findAvailableParams(Method method) {
        Map<Boolean, List<Parameter>> params = Arrays.stream(method.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(PaneliteParam.class))
                .collect(Collectors.partitioningBy(parameter -> parameter.getAnnotation(PaneliteParam.class).type() == PaneliteParamType.COMPLEX));
        List<Parameter> complexParams = params.get(true);
        List<Parameter> simpleParams = params.get(false);
        Stream<ComplexPaneliteParamDto> complexDtos = complexParams.stream()
                .map(parameter -> new ComplexPaneliteParamDto(parameter.getAnnotation(PaneliteParam.class).name(), findComplexTypeFields(parameter.getType())));
        Stream<SimplePaneliteParamDto> simpleDtos = simpleParams.stream()
                .map(parameter -> parameter.getAnnotation(PaneliteParam.class))
                .map(pp -> new SimplePaneliteParamDto(pp.name(), pp.type()));

        return Stream.concat(complexDtos, simpleDtos).toList();
    }

    private List<PaneliteParamDto> findComplexTypeFields(Class<?> type) {
        Map<Boolean, List<Field>> fields = Arrays.stream(type.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(PaneliteParam.class))
                .collect(Collectors.partitioningBy(field -> field.getAnnotation(PaneliteParam.class).type() == PaneliteParamType.COMPLEX));
        List<Field> complexFields = fields.get(true);
        List<Field> simpleFields = fields.get(false);

        Stream<ComplexPaneliteParamDto> complexDtos = complexFields.stream()
                .map(field -> new ComplexPaneliteParamDto(field.getAnnotation(PaneliteParam.class).name(), findComplexTypeFields(field.getType())));
        Stream<SimplePaneliteParamDto> simpleDtos = simpleFields.stream()
                .map(field -> field.getAnnotation(PaneliteParam.class))
                .map(pp -> new SimplePaneliteParamDto(pp.name(), pp.type()));

        return Stream.concat(complexDtos, simpleDtos).toList();
    }

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

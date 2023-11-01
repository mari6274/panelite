package dev.maczkowski.panelite.engine;

import dev.maczkowski.panelite.engine.api.PaneliteGroup;
import dev.maczkowski.panelite.engine.api.PaneliteMethod;
import dev.maczkowski.panelite.engine.api.PaneliteParam;
import dev.maczkowski.panelite.engine.api.PaneliteParamType;
import dev.maczkowski.panelite.engine.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PaneliteOperationsResolver {


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
}

package dev.maczkowski.panelite.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.maczkowski.panelite.engine.api.PaneliteParam;
import dev.maczkowski.panelite.engine.dto.PaneliteGroupDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerEndpoint(id = "paneliteApi")
@Validated
@RequiredArgsConstructor
public class PaneliteApiController {

    private final PaneliteService paneliteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/endpoints")
    public List<PaneliteGroupDto> endpoints() {
        return paneliteService.findAvailableOperations();
    }

    @PostMapping("/groups/{group}/methods/{method}")
    public ResponseEntity<Void> postMethod(@PathVariable @Pattern(regexp = "^[a-zA-z0-9_]+$") String group,
                                           @PathVariable @Pattern(regexp = "^[a-zA-z0-9_]+$") String method,
                                           @RequestBody @Valid JsonNode body,
                                           Principal principal) {
        log.info("Panelite COMMAND execution. User: {}, group: {}, method: {}, body: {}", principal.getName(), group, method, body);
        return paneliteService.findCommandObjectMethod(group, method)
                .map(om -> {
                    Type parameterType = om.method().getGenericParameterTypes()[0];
                    JavaType javaType = objectMapper.constructType(parameterType);
                    try {
                        Object args = objectMapper.treeToValue(body, javaType);
                        om.method().invoke(om.object(), args);
                    } catch (IllegalAccessException | InvocationTargetException | JsonProcessingException e) {
                        return ResponseEntity.badRequest().<Void>build();
                    }
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/groups/{group}/methods/{method}")
    public ResponseEntity<?> getMethod(@PathVariable @Pattern(regexp = "^[a-zA-z0-9_]+$") String group,
                                       @PathVariable @Pattern(regexp = "^[a-zA-z0-9_]+$") String method,
                                       @RequestParam Map<String, String> allRequestParams,
                                       Principal principal) {
        log.info("Panelite QUERY execution. User: {}, group: {}, method: {}, params: {}", principal.getName(), group, method, allRequestParams);
        return paneliteService.findQueryObjectMethod(group, method)
                .map(om -> {
                    try {
                        Method javaMethod = om.method();
                        PaneliteParam[] paneliteParams = Arrays.stream(javaMethod.getParameterAnnotations())
                                .map(Arrays::stream)
                                .map(annotationStream -> annotationStream
                                        .filter(a -> a.annotationType() == PaneliteParam.class)
                                        .findFirst()
                                        .orElse(null))
                                .filter(Objects::nonNull)
                                .map(PaneliteParam.class::cast)
                                .toArray(PaneliteParam[]::new);
                        if (javaMethod.getParameterCount() != paneliteParams.length) {
                            throw new IllegalStateException("Every param need to be annotated");
                        }

                        Object[] args = new Object[paneliteParams.length];
                        for (int i = 0; i < paneliteParams.length; i++) {
                            PaneliteParam paneliteParam = paneliteParams[i];
                            args[i] = parseParam(allRequestParams.get(paneliteParam.name()), javaMethod.getGenericParameterTypes()[i]);
                        }


                        Object result = javaMethod.invoke(om.object(), args);
                        return ResponseEntity.ok(result);
                    } catch (IllegalAccessException | InvocationTargetException | JsonProcessingException e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Object parseParam(String queryParam, Type parameterType) throws JsonProcessingException {
        JavaType javaType = objectMapper.constructType(parameterType);
        JsonNode jsonNode = objectMapper.readTree("{\"field\": \"" + queryParam + "\"}");
        return objectMapper.treeToValue(jsonNode.get("field"), javaType);
    }

}

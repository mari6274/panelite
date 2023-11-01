package dev.maczkowski.panelite.engine;

import dev.maczkowski.panelite.engine.api.PaneliteMethodType;
import dev.maczkowski.panelite.engine.api.PaneliteParamType;
import dev.maczkowski.panelite.engine.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@ControllerEndpoint(id = "panelite")
@RequiredArgsConstructor
public class PaneliteJspController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final PaneliteService paneliteService;

    @GetMapping
    public String home(Model model) {
//        model.addAttribute("groups", groupsStub());
        model.addAttribute("groups", paneliteService.findAvailableOperations());
        model.addAttribute("applicationName", applicationName);
        return "v-panelite";
    }

    private static List<PaneliteGroupDto> groupsStub() {
        return List.of(
                new PaneliteGroupDto("group1", List.of(
                        new PaneliteMethodDto("method1", PaneliteMethodType.QUERY, List.of(
                                new SimplePaneliteParamDto("param1", PaneliteParamType.TEXT),
                                new SimplePaneliteParamDto("param2", PaneliteParamType.INTEGER)
                        )),
                        new PaneliteMethodDto("method2", PaneliteMethodType.COMMAND, List.of(
                                new SimplePaneliteParamDto("param1", PaneliteParamType.BOOLEAN)
                        ))

                )),
                new PaneliteGroupDto("group2", List.of()),
                new PaneliteGroupDto("hello", List.of(
                        new PaneliteMethodDto("say_hello3", PaneliteMethodType.QUERY, List.of(
                                new SimplePaneliteParamDto("name", PaneliteParamType.TEXT),
                                new SimplePaneliteParamDto("number", PaneliteParamType.DECIMAL)
                        ))
                )),
                new PaneliteGroupDto("hello", List.of(
                        new PaneliteMethodDto("post_data", PaneliteMethodType.COMMAND, List.of(
                                new ComplexPaneliteParamDto("data", List.of(
                                        new SimplePaneliteParamDto("uid", PaneliteParamType.TEXT),
                                        new ComplexPaneliteParamDto("param", List.of(
                                                new SimplePaneliteParamDto("key", PaneliteParamType.TEXT),
                                                new SimplePaneliteParamDto("value", PaneliteParamType.TEXT)
                                        ))
                                ))
                        ))
                ))
        );
    }
}

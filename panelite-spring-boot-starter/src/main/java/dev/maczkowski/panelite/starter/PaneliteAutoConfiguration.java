package dev.maczkowski.panelite.starter;

import dev.maczkowski.panelite.engine.PaneliteApiController;
import dev.maczkowski.panelite.engine.PaneliteConfiguration;
import dev.maczkowski.panelite.engine.PaneliteJspController;
import dev.maczkowski.panelite.engine.PaneliteService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(PaneliteConfiguration.class)
public class PaneliteAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaneliteService paneliteService(ApplicationContext applicationContext) {
        return new PaneliteService(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public PaneliteApiController paneliteApiController(PaneliteService paneliteService) {
        return new PaneliteApiController(paneliteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PaneliteJspController paneliteJspController(PaneliteService paneliteService) {
        return new PaneliteJspController(paneliteService);
    }

}

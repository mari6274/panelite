package dev.maczkowski.panelite.starter;

import dev.maczkowski.panelite.engine.*;
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
    public PaneliteExecutor paneliteService(ApplicationContext applicationContext) {
        return new PaneliteExecutor(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public PaneliteOperationsResolver paneliteOperationsResolver(ApplicationContext applicationContext) {
        return new PaneliteOperationsResolver(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public PaneliteApiController paneliteApiController(PaneliteExecutor paneliteExecutor) {
        return new PaneliteApiController(paneliteExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    public PaneliteJspController paneliteJspController(PaneliteOperationsResolver paneliteOperationsResolver) {
        return new PaneliteJspController(paneliteOperationsResolver);
    }

}

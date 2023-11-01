package dev.maczkowski.panelite.engine;

import dev.maczkowski.panelite.engine.api.PaneliteGroup;
import dev.maczkowski.panelite.engine.groups.DuplicatedBean1;
import dev.maczkowski.panelite.engine.groups.DuplicatedBean2;
import dev.maczkowski.panelite.engine.groups.DuplicatedMethodsBean;
import dev.maczkowski.panelite.engine.groups.ValidBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaneliteServiceTest {

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private PaneliteService paneliteService;

    @Test
    void shouldFindCommandMethod() {
        ValidBean validBean = new ValidBean();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of("example_bean", validBean));

        var objectMethod = paneliteService.findCommandObjectMethod("validGroup", "validCommand");

        assertThat(objectMethod).isPresent();
        assertThat(objectMethod.get().object()).isSameAs(validBean);
        assertThat(objectMethod.get().method()).isNotNull();
    }

    @Test
    void shouldNotFindCommandMethodIfItIsAQuery() {
        ValidBean validBean = new ValidBean();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of("example_bean", validBean));

        var objectMethod = paneliteService.findCommandObjectMethod("validGroup", "validQuery");

        assertThat(objectMethod).isNotPresent();
    }


    @Test
    void shouldFindQueryMethod() {
        ValidBean validBean = new ValidBean();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of("example_bean", validBean));

        var objectMethod = paneliteService.findQueryObjectMethod("validGroup", "validQuery");

        assertThat(objectMethod).isPresent();
        assertThat(objectMethod.get().object()).isSameAs(validBean);
        assertThat(objectMethod.get().method()).isNotNull();
    }

    @Test
    void shouldNotFindQueryMethodIfItIsACommand() {
        ValidBean validBean = new ValidBean();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of("example_bean", validBean));

        var objectMethod = paneliteService.findQueryObjectMethod("validGroup", "validCommand");

        assertThat(objectMethod).isNotPresent();
    }

    @Test
    void shouldThrowExceptionIfDuplicatedCommand() {
        DuplicatedMethodsBean duplicatedMethodsBean = new DuplicatedMethodsBean();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of("example_bean", duplicatedMethodsBean));

        Exception exception = catchException(() -> paneliteService.findCommandObjectMethod("duplicatedMethodsGroup",
                "duplicatedCommand"));

        assertThat(exception).isNotNull()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple PaneliteMethod methods with the same name");
    }

    @Test
    void shouldThrowExceptionIfDuplicatedQuery() {
        DuplicatedMethodsBean duplicatedMethodsBean = new DuplicatedMethodsBean();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of("example_bean", duplicatedMethodsBean));

        Exception exception = catchException(() -> paneliteService.findQueryObjectMethod("duplicatedMethodsGroup",
                "duplicatedQuery"));

        assertThat(exception).isNotNull()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple PaneliteMethod methods with the same name");
    }

    @Test
    void shouldThrowExceptionIfDuplicatedGroupWhileFindingCommand() {
        DuplicatedBean1 duplicatedBean1 = new DuplicatedBean1();
        DuplicatedBean2 duplicatedBean2 = new DuplicatedBean2();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of(
                        "example_bean1", duplicatedBean1,
                        "example_bean2", duplicatedBean2));

        Exception exception = catchException(() -> paneliteService.findCommandObjectMethod("duplicatedGroup", "anything"
        ));

        assertThat(exception).isNotNull()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple PaneliteGroup beans with the same name");
    }

    @Test
    void shouldThrowExceptionIfDuplicatedGroupWhileFindingCommandUsingTheSameClass() {
        DuplicatedBean1 duplicatedBean1 = new DuplicatedBean1();
        DuplicatedBean1 duplicatedBean2 = new DuplicatedBean1();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of(
                        "example_bean1", duplicatedBean1,
                        "example_bean2", duplicatedBean2));

        Exception exception = catchException(() -> paneliteService.findCommandObjectMethod("duplicatedGroup", "anything"
        ));

        assertThat(exception).isNotNull()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple PaneliteGroup beans with the same name");
    }

    @Test
    void shouldThrowExceptionIfDuplicatedGroupWhileFindingQuery() {
        DuplicatedBean1 duplicatedBean1 = new DuplicatedBean1();
        DuplicatedBean2 duplicatedBean2 = new DuplicatedBean2();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of(
                        "example_bean1", duplicatedBean1,
                        "example_bean2", duplicatedBean2));

        Exception exception = catchException(() -> paneliteService.findQueryObjectMethod("duplicatedGroup", "anything"
        ));

        assertThat(exception).isNotNull()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple PaneliteGroup beans with the same name");
    }

    @Test
    void shouldThrowExceptionIfDuplicatedGroupWhileFindingQueryUsingTheSameClass() {
        DuplicatedBean1 duplicatedBean1 = new DuplicatedBean1();
        DuplicatedBean1 duplicatedBean2 = new DuplicatedBean1();
        when(applicationContext.getBeansWithAnnotation(PaneliteGroup.class))
                .thenReturn(Map.of(
                        "example_bean1", duplicatedBean1,
                        "example_bean2", duplicatedBean2));

        Exception exception = catchException(() -> paneliteService.findQueryObjectMethod("duplicatedGroup", "anything"
        ));

        assertThat(exception).isNotNull()
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Multiple PaneliteGroup beans with the same name");
    }
}

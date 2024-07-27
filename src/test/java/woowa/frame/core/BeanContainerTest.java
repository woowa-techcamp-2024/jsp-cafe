package woowa.frame.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.frame.core.annotation.Primary;
import woowa.frame.core.mock.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeanContainerTest {

    @AfterEach
    public void after() {
        BeanContainer.getInstance().clear();
    }

    @DisplayName("아무런 의존 관계가 없는 클래스 빈을 등록하면 생성된 빈을 조회할 수 있다.")
    @Test
    public void registerAndCreate() {
        // given
        var container = BeanContainer.getInstance();

        // when
        container.registerBean(BaseConstructorClass.class);

        // then
        BaseConstructorClass bean = container.getBean(BaseConstructorClass.class);
        assertThat(bean).isInstanceOf(BaseConstructorClass.class)
                .isNotNull();
    }

    @DisplayName("의존 관계가 있는 클래스 빈은 모든 의존 관계를 컨테이너에서 주입할 수 있으면 생성된 빈을 조회할 수 있다.")
    @Test
    public void registerAndCreateWithDependency() {
        // given
        var container = BeanContainer.getInstance();
        container.registerBean(BaseConstructorClass.class);

        // when
        container.registerBean(BaseConstructorClass.class);
        container.registerBean(NeedBaseConstructorClass.class);

        // then
        NeedBaseConstructorClass bean = container.getBean(NeedBaseConstructorClass.class);
        assertThat(bean).isInstanceOf(NeedBaseConstructorClass.class)
                .isNotNull();
    }

    @DisplayName("의존 관계가 있는 클래스 빈을 등록했지만 의존 관계를 객체를 컨테이너가 생성할 수 없다면 빈을 조회할 수 없다.")
    @Test
    public void registerAndCreateWithDependencyFail() {
        // given
        var container = BeanContainer.getInstance();
        container.registerBean(NeedBaseConstructorClass.class);

        // when
        container.registerBean(NeedBaseConstructorClass.class);

        // then
        assertThatThrownBy(() -> container.getBean(NeedBaseConstructorClass.class))
                .hasMessage("빈으로 등록되진 않은 클래스입니다. class=" + BaseConstructorClass.class.getName());
    }

    @DisplayName("인터페이스를 의존 하는 객체는 그 구현체를 주입하여 빈을 생성할 수 있다.")
    @Test
    public void registerAndCreateWithInterface() {
        // given
        var container = BeanContainer.getInstance();
        container.registerBean(MockInterfaceImpl1.class);

        // when
        container.registerBean(MockInterfaceImpl1.class);
        container.registerBean(NeedMockInterfaceClass.class);

        // then
        var bean = container.getBean(NeedMockInterfaceClass.class);
        assertThat(bean).isInstanceOf(NeedMockInterfaceClass.class)
                .isNotNull();
    }

    @DisplayName("인터페이스를 의존하는 객체를 생성할 때, 후보가 2가지라면 @Primary 어노테이션이 붙은 빈을 주입한다.")
    @Test
    public void registerAndCreateWithInterfacePrimary() {
        // given
        var container = BeanContainer.getInstance();
        container.registerBean(MockInterfaceImpl1.class);
        container.registerBean(MockInterfaceImpl2.class);

        // when
        container.registerBean(MockInterfaceImpl1.class);
        container.registerBean(MockInterfaceImpl2.class);
        container.registerBean(NeedMockInterfaceClass.class);

        // then
        var bean = container.getBean(NeedMockInterfaceClass.class);
        assertThat(bean).isInstanceOf(NeedMockInterfaceClass.class)
                .isNotNull();
        MockInterface injectedObject = bean.getMockInterface();
        assertThat(injectedObject).isInstanceOf(MockInterfaceImpl1.class);
        assertThat(injectedObject.getClass()).hasAnnotations(Primary.class);
    }
}
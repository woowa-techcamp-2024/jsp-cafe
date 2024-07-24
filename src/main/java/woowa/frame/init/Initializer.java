package woowa.frame.init;

import woowa.frame.core.BeanContainer;

public interface Initializer {

    /**
     * 처음 어플리케이션이 가동될 때, 한번 수행되는 메서드입니다. <br/>
     * 해당 인터페이스를 구현한다면 자동으로 초기화 로직이 실행됩니다. <br/>
     *
     * @see AppInitializer 초기화 메서드를 호출하는 초기화 핸들러
     * @param beanContainer 등록된 빈을 조회할 수 있는 빈 컨테이너
     */
    void init(BeanContainer beanContainer);
}

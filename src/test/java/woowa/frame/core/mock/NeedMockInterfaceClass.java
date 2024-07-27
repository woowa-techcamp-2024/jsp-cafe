package woowa.frame.core.mock;

public class NeedMockInterfaceClass {
    private final MockInterface mockInterface;

    public NeedMockInterfaceClass(MockInterface mockInterface) {
        this.mockInterface = mockInterface;
    }

    public MockInterface getMockInterface() {
        return mockInterface;
    }
}

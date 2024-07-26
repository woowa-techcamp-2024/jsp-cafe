package codesquad.fixture.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public interface SimpleRequestFixture extends EmptyRequestResponseFixture {
    default HttpServletRequest getRequest() {
        return new HttpServletRequestWrapper(emptyRequest()) {
            @Override
            public String getMethod() {
                return "GET";
            }
        };
    }

    default HttpServletRequest postRequest() {
        return new HttpServletRequestWrapper(emptyRequest()) {
            @Override
            public String getMethod() {
                return "POST";
            }
        };
    }
}

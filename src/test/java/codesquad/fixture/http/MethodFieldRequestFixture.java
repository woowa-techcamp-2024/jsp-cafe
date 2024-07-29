package codesquad.fixture.http;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public interface MethodFieldRequestFixture {
    default HttpServletRequest getFieldRequest(HttpServletRequest req) {
        return new HttpServletRequestWrapper(req) {
            @Override
            public DispatcherType getDispatcherType() {
                return DispatcherType.REQUEST;
            }

            @Override
            public String getMethod() {
                return "POST";
            }

            @Override
            public String getParameter(String name) {
                if (name.equals("_method")) {
                    return "GET";
                }
                return super.getParameter(name);
            }
        };
    }

    default HttpServletRequest postFieldRequest(HttpServletRequest req) {
        return new HttpServletRequestWrapper(req) {
            @Override
            public DispatcherType getDispatcherType() {
                return DispatcherType.REQUEST;
            }

            @Override
            public String getMethod() {
                return "POST";
            }

            @Override
            public String getParameter(String name) {
                if (name.equals("_method")) {
                    return "POST";
                }
                return super.getParameter(name);
            }
        };
    }

    default HttpServletRequest putFieldRequest(HttpServletRequest req) {
        return new HttpServletRequestWrapper(req) {
            @Override
            public DispatcherType getDispatcherType() {
                return DispatcherType.REQUEST;
            }

            @Override
            public String getMethod() {
                return "POST";
            }

            @Override
            public String getParameter(String name) {
                if (name.equals("_method")) {
                    return "PUT";
                }
                return super.getParameter(name);
            }
        };
    }

    default HttpServletRequest deleteFieldRequest(HttpServletRequest req) {
        return new HttpServletRequestWrapper(req) {
            @Override
            public DispatcherType getDispatcherType() {
                return DispatcherType.REQUEST;
            }

            @Override
            public String getMethod() {
                return "POST";
            }

            @Override
            public String getParameter(String name) {
                if (name.equals("_method")) {
                    return "DELETE";
                }
                return super.getParameter(name);
            }
        };
    }
}

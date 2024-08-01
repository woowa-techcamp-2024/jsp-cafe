package com.codesquad.cafe;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class E2ETestBase {

    private static final Logger logger = LoggerFactory.getLogger(E2ETestBase.class);

    private static Tomcat tomcat;

    protected static Context context;

    protected static int port;

    private static final RedirectStrategy neverRedirectStrategy = new RedirectStrategy() {
        @Override
        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
            return false;
        }

        @Override
        public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) {
            return null;
        }
    };

    @BeforeAll
    public static synchronized void setUpClass() throws Exception {
        if (tomcat == null) {
            tomcat = startEmbeddedTomcat();
            port = tomcat.getConnector().getLocalPort();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    stopEmbeddedTomcat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    private static Tomcat startEmbeddedTomcat() throws Exception {
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(0);
        port = tomcat.getConnector().getPort();

        context = addWebApp(tomcat);
        addClassesToWebInfClasses(context);
        setErrorReportValue(tomcat);

        tomcat.enableNaming();
        addContextConfig(context);

        tomcat.start();

        logger.info("context initialized with {} servlet mappings, {} context listener",
                context.findApplicationListeners().length,
                context.findServletMappings().length);

        return tomcat;
    }

    private static Context addWebApp(Tomcat tomcat) {
        String webAppPath = new File("src/main/webapp").getAbsolutePath();
        return tomcat.addWebapp("", webAppPath);
    }

    private static void setErrorReportValue(Tomcat tomcat) {
        ErrorReportValve errorReportValve = new ErrorReportValve();
        errorReportValve.setShowReport(false);
        errorReportValve.setShowServerInfo(false);
        tomcat.getHost().getPipeline().addValve(errorReportValve);
    }

    private static void addClassesToWebInfClasses(Context context) {
        File additionWebInfClasses = new File("build/classes/java/main");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);
    }

    private static void addContextConfig(Context context) throws MalformedURLException {
        URL contextXml = new File("src/test/resources/context.xml").toURI().toURL();
        context.setConfigFile(contextXml);
    }

    private static void stopEmbeddedTomcat() throws LifecycleException {
        if (tomcat != null) {
            tomcat.stop();
            tomcat.destroy();
            tomcat = null;
        }
    }

    // path should start with "/"
    protected SavedHttpResponse get(String path) throws IOException {

        HttpGet httpGet = new HttpGet("http://localhost:" + port + path);
        try (CloseableHttpClient client = HttpClients.custom()
                .setRedirectStrategy(neverRedirectStrategy)
                .build();
             CloseableHttpResponse response = client.execute(httpGet)) {
            return new SavedHttpResponse(response.getStatusLine(), response.getAllHeaders(),
                    EntityUtils.toString(response.getEntity()));
        }
    }

    protected SavedHttpResponse get(String path, String sessionId) throws IOException {
        HttpGet httpGet = new HttpGet("http://localhost:" + port + path);
        httpGet.addHeader("Cookie", "JSESSIONID=" + sessionId);
        try (CloseableHttpClient client = HttpClients.custom()
                .setRedirectStrategy(neverRedirectStrategy)
                .build();
             CloseableHttpResponse response = client.execute(httpGet)) {
            response.getStatusLine();
            response.getAllHeaders();
            response.getEntity();
            return new SavedHttpResponse(response.getStatusLine(), response.getAllHeaders(),
                    EntityUtils.toString(response.getEntity()));
        }
    }

    // path should start with "/"
    protected HttpResponse post(String path, String body) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("http://localhost:" + port + path);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setEntity(new StringEntity(body));
            CloseableHttpResponse response = client.execute(request);
            return response;
        }
    }

    protected HttpResponse post(String path, String body, String sessionId) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("http://localhost:" + port + path);
            request.addHeader("Cookie", "JSESSIONID=" + sessionId);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setEntity(new StringEntity(body));
            CloseableHttpResponse response = client.execute(request);
            return response;
        }
    }

    protected SavedHttpResponse put(String path, String body, String sessionId) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://localhost:" + port + path);
            if (sessionId != null) {
                request.addHeader("Cookie", "JSESSIONID=" + sessionId);
            }
            request.setHeader("Content-Type", "application/json;charset=UTF-8");
            request.setEntity(new StringEntity(body));
            CloseableHttpResponse response = client.execute(request);
            return new SavedHttpResponse(response.getStatusLine(), response.getAllHeaders(),
                    EntityUtils.toString(response.getEntity()));
        }
    }

}

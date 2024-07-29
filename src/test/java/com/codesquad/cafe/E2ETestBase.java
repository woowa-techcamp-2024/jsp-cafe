package com.codesquad.cafe;

import static com.codesquad.cafe.TestDataSource.dataSource;

import java.io.File;
import java.io.IOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class E2ETestBase {
    private static final Logger logger = LoggerFactory.getLogger(E2ETestBase.class);

    private static Tomcat tomcat;

    protected static Context context;

    protected static int port;

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
        configContextXml(context);

        setErrorReportValue(tomcat);

        tomcat.start();

//        configureDataSource();
//        createTable();

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

    private static void configContextXml(Context context) {
        StandardContext ctx = (StandardContext) context;
        ContextResource resource = new ContextResource();
        resource.setName("jdbc/cafe");
        resource.setType(DataSource.class.getName());
        resource.setProperty("driverClassName", "org.h2.Driver");
        resource.setProperty("url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        resource.setProperty("username", "sa");
        resource.setProperty("password", "");
        resource.setProperty("maxTotal", "4");
        ctx.getNamingResources().addResource(resource);
//        //set the configuration file for the context
//        URL contextXml = new File("src/test/resources/context.xml").toURI().toURL();
//        context.setConfigFile(contextXml);
//        context.gethos
////        //set the configuration for the web application
    }

    private static void configureDataSource() throws NamingException {
        try {
            InitialContext context = new InitialContext();
            context.createSubcontext("java:/comp");
            context.createSubcontext("java:/comp/env");
            context.createSubcontext("java:/comp/env/jdbc");
            context.bind("java:/comp/env/jdbc/cafe", dataSource());
        } catch (Exception e) {
            System.out.println("context already exists");
        }
    }

    private static void createTable() {
        TestDataSource testDataSource = new TestDataSource();
        testDataSource.createTable(dataSource());
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
        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(new HttpGet("http://localhost:" + port + path));) {
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
}

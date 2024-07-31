package org.example.cafe.e2e;

import java.io.File;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;

abstract public class TomcatBaseTestEnvironment {

    protected static Tomcat tomcat;
    protected static int localPort;

    protected static UserRepository userRepository;
    protected static QuestionRepository questionRepository;
    protected static ReplyRepository replyRepository;

    static {
        String webappPath = new File("src/main/webapp").getAbsolutePath();

        tomcat = new Tomcat();
        tomcat.setPort(0);

        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));

        StandardContext context = (StandardContext) tomcat.addWebapp("", new File(webappPath).getAbsolutePath());

        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        localPort = tomcat.getConnector().getLocalPort();
        HttpUrlConnectionUtils.setLocalPort(localPort);

        userRepository = (UserRepository) context.getServletContext().getAttribute("UserRepository");
        questionRepository = (QuestionRepository) context.getServletContext().getAttribute("QuestionRepository");
        replyRepository = (ReplyRepository) context.getServletContext().getAttribute("ReplyRepository");
    }

    @BeforeEach
    public void setUpEach() {
        replyRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }
}

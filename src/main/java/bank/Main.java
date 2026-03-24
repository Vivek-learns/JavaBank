package bank;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        String port = System.getenv("PORT");
        if (port == null) port = "8080";

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        tomcat.getConnector(); // triggers connector creation

        // Point to your webapp directory
        String webappDir = new File("src/main/webapp").getAbsolutePath();

        // Use addWebapp for full web.xml + JSP support
        Context context = tomcat.addWebapp("", webappDir);

        // Add target/classes to the webapp's classloader so Tomcat can
        // find the servlet classes declared in web.xml
        File classesDir = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(
                resources, "/WEB-INF/classes",
                classesDir.getAbsolutePath(), "/"));
        context.setResources(resources);

        tomcat.start();
        System.out.println("JavaBank started on port " + port);
        tomcat.getServer().await();
    }
}
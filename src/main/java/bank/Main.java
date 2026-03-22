package bank;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
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
        
        // "" means root context path
        tomcat.addWebapp("", webappDir);

        tomcat.start();
        System.out.println("JavaBank started on port " + port);
        tomcat.getServer().await();
    }
}
package sec.project;

import org.apache.catalina.Context;
import org.h2.tools.RunScript;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication implements EmbeddedServletContainerCustomizer {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(CyberSecurityBaseProjectApplication.class);
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./database", "sa", "");

        try {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("src/sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("src/sql/database-import.sql"));
        } catch (Throwable ignored) { }

        connection.close();
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer cesc) {
        ((TomcatEmbeddedServletContainerFactory) cesc).addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context cntxt) {
                // Caused some red error on prod so we disabled it. We're unhackable anyways.
                cntxt.setUseHttpOnly(false);
            }

        });
    }
}

package br.com.pvprojects.horario;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "br.com.pvprojects", exclude = MongoAutoConfiguration.class)
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        ConfigurableEnvironment environment = context.getEnvironment();
        String port = environment.getProperty("server.port");
        String name = environment.getProperty("spring.application.name");
        String hostAddress = null;

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Erro ao buscar o host", e);
        }

        log.info("\n" +
            "\n" +
            "| \n" +
            "| ------------------------------------------------------------\n" +
            "| Application: \'" + name + "\' is running! \n" +
            "| Access URL: \n" +
            "| Host -> " + hostAddress + ":" + port + "\n" +
            "| ------------------------------------------------------------\n" +
            "| \n");
    }
}
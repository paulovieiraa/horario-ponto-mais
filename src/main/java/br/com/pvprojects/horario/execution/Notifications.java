package br.com.pvprojects.horario.execution;

import br.com.pvprojects.horario.service.HorarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class Notifications {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final HorarioService service;

    public Notifications(HorarioService service) {
        this.service = service;
    }

    /**
     * JORNADA
     *
     * <p> Iniciou o dia de trabalho </p>
     * <p> Iniciou o intervalo </p>
     * <p> Voltou a trabalhar </p>
     * <p> Parou de trabalhar </p>
     */

    @Scheduled(cron = "${batch.cron.t1}")
    void runJobT1() {
        log.info("Executing job to notify.");

        service.job("Ponto: Iniciou o dia de trabalho");

        log.info("Finished job to notify.");
    }

    @Scheduled(cron = "${batch.cron.t2}")
    void runJobT2() {
        log.info("Executing job to notify.");

        service.job("Ponto: Iniciou o intervalo");

        log.info("Finished job to notify.");
    }

    @Scheduled(cron = "${batch.cron.t3}")
    void runJobT3() {
        log.info("Executing job to notify ");

        service.job("Ponto: Voltou a trabalhar");

        log.info("Finished job to notify ");
    }

    @Scheduled(cron = "${batch.cron.t4}")
    void runJobT4() {
        log.info("Executing job to notify ");

        service.job("Ponto: Parou de trabalhar");

        log.info("Finished job to notify ");
    }
}
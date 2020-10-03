package br.com.pvprojects.horario;

import br.com.pvprojects.horario.service.HorarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/email")
public class TestController {

    private final HorarioService service;

    public TestController(HorarioService service) {
        this.service = service;
    }

    @GetMapping(path = "/send")
    public void test() {
        service.job("Teste qa");
    }
}
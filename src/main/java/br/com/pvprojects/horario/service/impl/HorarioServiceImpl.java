package br.com.pvprojects.horario.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import br.com.pvprojects.horario.dto.Register;
import br.com.pvprojects.horario.mail.SendEmail;
import br.com.pvprojects.horario.service.HorarioService;

@Service
public class HorarioServiceImpl implements HorarioService {

    private static final Logger log = LoggerFactory.getLogger(HorarioServiceImpl.class);
    private final String URL_LOGIN = "https://api.pontomais.com.br/api/auth/sign_in";
    private final String URL_REGISTER = "https://api.pontomais.com.br/api/time_cards/register";

    @Value("${spring.mail.username}")
    private String remetente;

    private HttpResponse<JsonNode> jsonResponse;
    private String token;
    private String clientId;
    private String email;
    private String nome;
    private String expiryTime;

    private final SendEmail sendEmail;

    public HorarioServiceImpl(SendEmail sendEmail) {
        this.sendEmail = sendEmail;
    }

    @Override
    public void job(String titulo) {

        Map<String, String> usuarios = new HashMap<>();
        usuarios.put("SEU_EMAIL", "SUA_SENHA");

        usuarios.forEach((k, v) -> {
            JsonObject json = new JsonObject();
            json.addProperty("login", k);
            json.addProperty("password", v);

            log.info("Inicio do login");
            jsonResponse = this.getLogin(json);

            if (jsonResponse != null && jsonResponse.getStatus() == 201) {
                log.info("Inicio /register:");

                HttpResponse registerResponse = this.getRegister();

                Map<String, Object> map = new HashMap<>();

                String template = "email/sucesso";
                map.put("nome", nome);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime data = LocalDateTime.now();
                map.put("data", dtf.format(data));

                if (registerResponse != null && registerResponse.getStatus() == 200) {
                    log.info("Prepando para enviar o email.");
                    try {
                        sendEmail
                                .enviarEmail(remetente, Arrays.asList(email), titulo, template, map);
                    } catch (Exception e) {
                        log.error("Erro no envio do email: {}", e.getMessage());
                    }
                }
            }
            token = null;
            clientId = null;
            email = null;
            expiryTime = null;
            nome = null;
        });
    }

    private HttpResponse getLogin(JsonObject json) throws JSONException {

        try {
            jsonResponse = Unirest.post(URL_LOGIN)
                    .headers(this.headersLogin())
                    .body(json.toString())
                    .asJson();
        } catch (UnirestException e) {
            log.error("Erro ao efetuar o login");
            e.printStackTrace();
        }

        if (jsonResponse.getStatus() == 201) {
            token = jsonResponse.getBody().getObject().get("token").toString();
            clientId = jsonResponse.getBody().getObject().get("client_id").toString();
            expiryTime = jsonResponse.getHeaders().get("expiry").get(0);
            email = jsonResponse.getBody().getObject().getJSONObject("data").get("email")
                    .toString();
            String[] splitted = email.split("\\.", 5);
            nome = splitted[0].trim().toUpperCase();

            log.info("Login efetuado para o usuario: {}", nome);
        }

        return jsonResponse;
    }

    private Map<String, String> headersLogin() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        return header;
    }

    private HttpResponse getRegister() throws JSONException {
        Register register = new Register();

        ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module()).registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            jsonResponse = Unirest
                    .post(URL_REGISTER)
                    .headers(this.headersRegister())
                    .body(mapper.writeValueAsString(register))
                    .asJson();
        } catch (UnirestException | JsonProcessingException e) {
            log.error("Erro ao efetuar o login");
            e.printStackTrace();
        }

        log.info("Ponto realizado para o usuario: {}", nome);
        return jsonResponse;
    }

    private Map<String, String> headersRegister() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        header.put("access-token", token);
        header.put("Api-Version", "2");
        header.put("client", clientId);
        header.put("expiry", expiryTime);
        header.put("token-type", "Bearer");
        header.put("uid", email);
        header.put("uuid", UUID.randomUUID().toString());
        header.put("Origin", "https://app.pontomaisweb.com.br");
        header.put("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        return header;
    }
}
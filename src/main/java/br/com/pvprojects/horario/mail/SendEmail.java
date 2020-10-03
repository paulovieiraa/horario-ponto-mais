package br.com.pvprojects.horario.mail;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class SendEmail {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine thymeleaf;

    public void enviarEmail(String remetente, List<String> destinatarios, String assunto,
        String template,
        Map<String, Object> variaveis) {

        Context context = new Context(new Locale("pt-BR"));

        variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

        String mensagem = this.thymeleaf.process(template, context);
        this.enviarEmail(remetente, destinatarios, assunto, mensagem);
    }

    private void enviarEmail(String remetente, List<String> destinatarios, String assunto,
        String mensagem) {

        try {

            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            this.javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail.");
        }
    }
}
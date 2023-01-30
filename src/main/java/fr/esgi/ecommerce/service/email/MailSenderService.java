package fr.esgi.ecommerce.service.email;

import fr.esgi.ecommerce.configuration.ApplicationConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final ApplicationConfiguration applicationConfiguration;

    public void sendMessageHtml(String to, String subject, String template, Map<String, Object> attributes) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(attributes);
        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
        MimeMessagePreparator preparatory = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(applicationConfiguration.getMailSender().getSender());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
        };
        javaMailSender.send(preparatory);
    }
}

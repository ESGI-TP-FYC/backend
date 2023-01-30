package fr.esgi.ecommerce.configuration;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Data
@AllArgsConstructor
@Configuration
public class ApplicationConfiguration {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    @ConfigurationProperties(prefix = "application.document")
    public DocumentStorage getDocumentStorage() {
        return new DocumentStorage();
    }

    @Getter
    @Setter
    public static class DocumentStorage {
        private String uploadDirectory;
    }

    @Bean
    @ConfigurationProperties(prefix = "application.mail")
    public MailSender getMailSender() {
        return new MailSender();
    }
    @Getter
    @Setter
    public static class MailSender {
        private String sender;
    }
}

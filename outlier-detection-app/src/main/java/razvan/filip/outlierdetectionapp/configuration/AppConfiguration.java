package razvan.filip.outlierdetectionapp.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public NewTopic topic1() {
        return new NewTopic("readings", 1, (short) 1);
    }
}

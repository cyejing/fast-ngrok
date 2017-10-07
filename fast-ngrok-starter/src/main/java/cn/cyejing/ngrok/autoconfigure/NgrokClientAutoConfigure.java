package cn.cyejing.ngrok.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "ngrok", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(NgrokProperties.class)
public class NgrokClientAutoConfigure {

    @Bean
    public NgrokEmbeddedServletContainerInitializedEventListener
    embeddedServletContainerInitializedEventListener(NgrokProperties ngrokProperties) {

        return new NgrokEmbeddedServletContainerInitializedEventListener(ngrokProperties);
    }
}

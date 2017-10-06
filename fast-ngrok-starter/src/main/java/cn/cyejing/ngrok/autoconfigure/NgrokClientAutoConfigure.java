package cn.cyejing.ngrok.autoconfigure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NgrokProperties.class)
public class NgrokClientAutoConfigure {

    @Bean
    public NgrokEmbeddedServletContainerInitializedEventListener
        embeddedServletContainerInitializedEventListener(NgrokProperties ngrokProperties) {

        return new NgrokEmbeddedServletContainerInitializedEventListener(ngrokProperties);
    }
}

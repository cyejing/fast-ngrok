package cn.cyejing.ngrok.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NgrokClientAutoConfigure {

    @Bean
    public EmbeddedServletContainerInitializedEventListener embeddedServletContainerInitializedEventListener() {
        return new EmbeddedServletContainerInitializedEventListener();
    }
}

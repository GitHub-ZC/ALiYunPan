package top.iecoxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import top.iecoxe.utils.RtErrorHandler;

@EnableScheduling
@SpringBootApplication
public class ALiYunPanApplication {

    public static void main(String[] args) {
        SpringApplication.run(ALiYunPanApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        /*RestTemplate restTemplate = new RestTemplate();
        return restTemplate;*/

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false); // 解决401报错时，报java.net.HttpRetryException: cannot retry due to server authentication, in streaming mode
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        return restTemplate;
    }
}

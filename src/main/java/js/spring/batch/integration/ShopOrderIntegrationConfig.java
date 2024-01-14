package js.spring.batch.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;

import java.io.File;

@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
@Configuration
public class ShopOrderIntegrationConfig {

    @Value("${shop.user.directory}")
    private String shopUserDirectory;


    /* Gestore del File -- il filtro serve a selezionare
       la tipologia di file da leggere prevenire letture sporche ()
    */
    @Bean
    public FileReadingMessageSource fileReadingMessageSource() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(shopUserDirectory));
        messageSource.setFilter(new SimplePatternFileListFilter("*.csv"));
        return messageSource;
    }


    /*
     * Configure inbound channel
     */
    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

}

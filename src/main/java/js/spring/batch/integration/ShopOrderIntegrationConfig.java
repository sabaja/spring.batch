package js.spring.batch.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

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


    /*Per rinominare il file una volta letto e scritto*/
    @Bean
    public MessageHandler fileRenameProcessingHandler() {
        FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File(shopUserDirectory));
        fileWritingMessageHandler.setFileExistsMode(FileExistsMode.REPLACE);
        fileWritingMessageHandler.setFileNameGenerator(fileNamGenerator());
        fileWritingMessageHandler.setRequiresReply(Boolean.FALSE);
        return fileWritingMessageHandler;
    }

    private FileNameGenerator fileNamGenerator() {
        DefaultFileNameGenerator defaultFileNameGenerator = new DefaultFileNameGenerator();
        //con questa espressonio spring va a prendere il nome del payload e gli carica il prefisso processing
        defaultFileNameGenerator.setExpression("payload.name + '.processing'");
        return defaultFileNameGenerator;
    }

}

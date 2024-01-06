package js.spring.batch;

import js.spring.batch.job.schedule.JobScheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext
                                                       context) {
        return args -> {
            // Ottieni il bean dal contesto
            JobScheduler myBean = context.getBean(JobScheduler.class);

            // Chiama il metodo del bean
            myBean.run();
        };
    }
}


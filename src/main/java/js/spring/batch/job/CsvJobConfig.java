package js.spring.batch.job;

import js.spring.batch.job.processor.ShopUserProcessor;
import js.spring.batch.model.ShopUserEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class CsvJobConfig {

    @Autowired
    private ShopUserRepository repository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job csvJob(JobRepository jobRepository, PlatformTransactionManager PlatformTransactionManager) {
        return new JobBuilder("csvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(csvStep())
                .build();

    }

    @Bean
    public Step csvStep() {
               return new StepBuilder("csvStep", jobRepository)
                .<ShopUserEntity, ShopUserEntity>chunk(10, platformTransactionManager)
                .reader(csvReader())
                .processor(new ShopUserProcessor())
                .writer(userWriter())
                .build();

    }


    @Bean
    public RepositoryItemWriter<ShopUserEntity> userWriter() {
        RepositoryItemWriter<ShopUserEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public FlatFileItemReader<ShopUserEntity> csvReader() {
        FlatFileItemReader<ShopUserEntity> writer = new FlatFileItemReader<>();
        writer.setName("csvReader");
        writer.setResource(new FileSystemResource("src/main/resources/data/users.csv"));
        writer.setLineMapper(lineMapper());
        return writer;
    }

    private ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                return LocalDateTime.parse(source, formatter);
            }
        });
        return service;
    }

    @Bean
    public LineMapper<ShopUserEntity> lineMapper() {
        /* Tokenizzatore della riga*/
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("username", "email", "createdAt");

        /* Contenitore del Bean da mappare */
        BeanWrapperFieldSetMapper<ShopUserEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ShopUserEntity.class);
        fieldSetMapper.setConversionService(conversionService());

        DefaultLineMapper<ShopUserEntity> lineMapper = new DefaultLineMapper<>();

        /* mapper della linea */
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}

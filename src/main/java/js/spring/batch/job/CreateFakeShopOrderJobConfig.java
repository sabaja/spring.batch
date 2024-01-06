package js.spring.batch.job;

import jakarta.persistence.EntityManagerFactory;
import js.spring.batch.dto.ShopOrderDto;
import js.spring.batch.job.processor.CreateUserProcessor;
import js.spring.batch.job.writer.CreateShopWriter;
import js.spring.batch.model.ShopProductEntity;
import js.spring.batch.model.ShopUserEntity;
import js.spring.batch.repository.ShopProductRepository;
import js.spring.batch.repository.ShopUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class CreateFakeShopOrderJobConfig {


    private final ShopUserRepository shopUserRepository;
    private final ShopProductRepository shopProductRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final CreateShopWriter createShopWriter;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job createFakeShopOrderJob() {
        return new JobBuilder("createFakeShopOrderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createUserStep())
//                .next(createProductStep())
//                .next(createOrderStep())
                .build();

    }

    @Bean
    public Step createUserStep() {
        return new StepBuilder("createUserStep", jobRepository)
                .<ShopUserEntity, Future<ShopUserEntity>>chunk(200, platformTransactionManager)
                .reader(createUserReader())
                .processor(asyncUserProcessor())
                .writer(asyncUserWriter())
                .taskExecutor(taskExecutor())
                .faultTolerant()
                .build();

    }


//    @Bean
//    public ItemStreamReader<ShopUserEntity> createUserReader() {
//        //flatFileItemReader is not thread safe
//        FlatFileItemReader<ShopUserEntity> reader = new FlatFileItemReader<>();
//        reader.setName("createUserReader");
//        reader.setResource(new FileSystemResource("src/main/resources/data/users.csv"));
//        reader.setLineMapper(createUserLineMapper());
//
//        return reader;
//    }

    @Bean
    public SynchronizedItemStreamReader<ShopUserEntity> createUserReader() {
        //flatFileItemReader is not thread safe
        FlatFileItemReader<ShopUserEntity> reader = new FlatFileItemReader<>();
        reader.setName("createUserReader");
        reader.setResource(new FileSystemResource("src/main/resources/data/users.csv"));
        reader.setLineMapper(createUserLineMapper());

        //https://docs.spring.io/spring-batch/reference/readers-and-writers/item-reader-writer-implementations.html
        //SynchronizedItemStreamReader in order to use it safely in a multi-threaded step
        return new SynchronizedItemStreamReaderBuilder<ShopUserEntity>()
                .delegate(reader)
                .build();
    }

    @Bean
    public AsyncItemProcessor<ShopUserEntity, ShopUserEntity> asyncUserProcessor() {
        AsyncItemProcessor<ShopUserEntity, ShopUserEntity> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(new CreateUserProcessor());
        asyncItemProcessor.setTaskExecutor(taskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public JpaItemWriter<ShopUserEntity> createUserWriter() {
        JpaItemWriter<ShopUserEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public AsyncItemWriter<ShopUserEntity> asyncUserWriter() {
        AsyncItemWriter<ShopUserEntity> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(createUserWriter());
        return asyncItemWriter;
    }

    @Bean
    public LineMapper<ShopUserEntity> createUserLineMapper() {
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

    @Bean
    public Step createProductStep() {
        return new StepBuilder("createProductStep", jobRepository)
                .<ShopProductEntity, ShopProductEntity>chunk(1, platformTransactionManager)
                .reader(createProductReader())
                .writer(createProductWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step createOrderStep() {
        return new StepBuilder("create", jobRepository)
                .<ShopOrderDto, ShopOrderDto>chunk(1, platformTransactionManager)
                .reader(createOrderReader())
                .writer(createShopWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public JpaItemWriter<ShopProductEntity> createProductWriter() {
        JpaItemWriter<ShopProductEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }


    @Bean
    public ItemReader<ShopProductEntity> createProductReader() {
        FlatFileItemReader<ShopProductEntity> reader = new FlatFileItemReader<>();
        reader.setName("createProductReader");
        reader.setResource(new FileSystemResource("src/main/resources/data/product.csv"));
        reader.setLineMapper(createProductLineMapper());
        return reader;
    }

    @Bean
    public LineMapper<ShopProductEntity> createProductLineMapper() {
        /* Tokenizzatore della riga*/
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("productName", "price");

        /* Contenitore del Bean da mappare */
        BeanWrapperFieldSetMapper<ShopProductEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ShopProductEntity.class);

        DefaultLineMapper<ShopProductEntity> lineMapper = new DefaultLineMapper<>();

        /* mapper della linea */
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


    @Bean
    public ItemReader<ShopOrderDto> createOrderReader() {
        FlatFileItemReader<ShopOrderDto> reader = new FlatFileItemReader<>();
        reader.setName("createOrderReader");
        reader.setResource(new FileSystemResource("src/main/resources/data/shop.csv"));
        reader.setLineMapper(createOrderLineMapper());
        return reader;
    }


    @Bean
    public LineMapper<ShopOrderDto> createOrderLineMapper() {
        /* Tokenizzatore della riga*/
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("product", "user", "quantity");

        BeanWrapperFieldSetMapper<ShopOrderDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ShopOrderDto.class);

        DefaultLineMapper<ShopOrderDto> lineMapper = new DefaultLineMapper<>();

        /* mapper della linea */
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;

    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(50);
        threadPoolTaskExecutor.setQueueCapacity(50); //Impostando un numero si ottiene una LinkedBlockingQueue asincrona
        threadPoolTaskExecutor.setThreadNamePrefix("Thread n°->");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
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
}












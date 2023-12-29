package js.spring.batch.job;

import js.spring.batch.dto.ShopOrderDto;
import js.spring.batch.job.processor.CreateUserProcessor;
import js.spring.batch.job.writer.CreateShopWriter;
import js.spring.batch.model.ShopProductEntity;
import js.spring.batch.model.ShopUserEntity;
import js.spring.batch.repository.ShopProductRepository;
import js.spring.batch.repository.ShopUserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
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
public class CreateFakeShopOrderJobConfig {

    @Autowired
    private ShopUserRepository shopUserRepository;
    @Autowired
    private ShopProductRepository shopProductRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private CreateShopWriter createShopWriter;

    @Bean
    public Job createFakeShopOrderJob() {
        return new JobBuilder("createFakeShopOrderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createUserStep())
                .next(createProductStep())
                .next(createOrderStep())
                .build();

    }

    @Bean
    public Step createUserStep() {
        return new StepBuilder("createUserStep", jobRepository)
                .<ShopUserEntity, ShopUserEntity>chunk(1, platformTransactionManager)
                .reader(createUserReader())
                .processor(new CreateUserProcessor())
                .writer(createUserWriter())
                .build();

    }

    @Bean
    public Step createProductStep() {
        return new StepBuilder("createProductStep", jobRepository)
                .<ShopProductEntity, ShopProductEntity>chunk(1, platformTransactionManager)
                .reader(createProductReader())
                .writer(createProductWriter())
                .build();
    }

    @Bean
    public Step createOrderStep() {
        return new StepBuilder("create", jobRepository)
                .<ShopOrderDto, ShopOrderDto>chunk(1, platformTransactionManager)
                .reader(createOrderReader())
                .writer(createShopWriter)
                .build();
    }

    @Bean
    public RepositoryItemWriter<ShopUserEntity> createUserWriter() {
        RepositoryItemWriter<ShopUserEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(shopUserRepository);
        writer.setMethodName("saveAndFlush");
        return writer;
    }

    @Bean
    public RepositoryItemWriter<ShopProductEntity> createProductWriter() {
        RepositoryItemWriter<ShopProductEntity> writer = new RepositoryItemWriter<>();
        writer.setRepository(shopProductRepository);
        writer.setMethodName("saveAndFlush");
        return writer;
    }

    @Bean
    public FlatFileItemReader<ShopUserEntity> createUserReader() {
        FlatFileItemReader<ShopUserEntity> reader = new FlatFileItemReader<>();
        reader.setName("createUserReader");
        reader.setResource(new FileSystemResource("src/main/resources/data/users.csv"));
        reader.setLineMapper(createUserLineMapper());
        return reader;
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
    public ItemReader<ShopOrderDto> createOrderReader() {
        FlatFileItemReader<ShopOrderDto> reader = new FlatFileItemReader<>();
        reader.setName("createOrderReader");
        reader.setResource(new FileSystemResource("src/main/resources/data/shop.csv"));
        reader.setLineMapper(createOrderLineMapper());
        return reader;
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
}












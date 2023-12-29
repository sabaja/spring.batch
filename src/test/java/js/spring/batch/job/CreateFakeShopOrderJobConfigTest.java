package js.spring.batch.job;

import org.junit.jupiter.api.AfterAll;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBatchTest
//@SpringJUnitConfig(CreateFakeShopOrderJobConfig.class)
public class CreateFakeShopOrderJobConfigTest {

    @Autowired
    @Qualifier("createFakeShopOrderJob")
    private Job job;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

//    @Autowired
//    public void setDataSource(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }


    @AfterAll
    public static void cleanUp() {
        JobRepositoryTestUtils repositoryTestUtils = new JobRepositoryTestUtils();
        repositoryTestUtils.removeJobExecutions();
    }

//    @Test
    public void testCreateFakeShopOrderJob() throws Exception {
        jobLauncherTestUtils.setJob(job);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Verify that the job succeeded
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

//        // Verify the number of items processed by each step
//        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
//
//        // Step 1: Create Users
//        StepExecution createUserStepExecution = stepExecutions.get(0);
//        assertThat(createUserStepExecution.getReadCount()).isEqualTo(100);
//        assertThat(createUserStepExecution.getWriteCount()).isEqualTo(100);
//
//        // Step 2: Create Products
//        StepExecution createProductStepExecution = stepExecutions.get(1);
//        assertThat(createProductStepExecution.getReadCount()).isEqualTo(50);
//        assertThat(createProductStepExecution.getWriteCount()).isEqualTo(50);
    }
}
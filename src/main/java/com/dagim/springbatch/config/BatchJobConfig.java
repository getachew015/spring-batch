package com.dagim.springbatch.config;

import com.dagim.springbatch.entity.Customer;
import com.dagim.springbatch.partitioning.ColumnRangePartitioning;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchJobConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerWriter customerWriter;

    @Bean
    public FlatFileItemReader<Customer> itemReader(){
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers-100000.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public CustomerItemProcessor itemProcessor(){
        return new CustomerItemProcessor();
    }

    @Bean
    public Step masterStep(){
        return stepBuilderFactory.get("csv-read-master-step")
                .partitioner(slaveStep().getName(), rangePartitioner() )
                .partitionHandler(partitionHandler())
                .build();
    }
    @Bean
    public Step slaveStep(){
        return stepBuilderFactory.get("csv-read-slave-step")
                .<Customer,Customer>chunk(1000)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(customerWriter)
                .build();
    }

    @Bean(name = "csvReadJob")
    public Job csvReadJob(){
        return jobBuilderFactory.get("csv-read-job")
                .flow(masterStep())
                .end().build();
    }
    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(10);
        return taskExecutor;
    }

    @Bean
    public ColumnRangePartitioning rangePartitioner(){
        ColumnRangePartitioning columnRangePartitioning = new ColumnRangePartitioning();
        columnRangePartitioning.setMaxRowCount(100000);
        return columnRangePartitioning;
    }

    public PartitionHandler partitionHandler(){
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(100);
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        taskExecutorPartitionHandler.setStep(slaveStep());
        return taskExecutorPartitionHandler;
    }
    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("Index","Customer_Id","First_Name","Last_Name","Company","City","Country","Phone_1","Phone_2","Email","Subscription_Date","Website");
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }


}

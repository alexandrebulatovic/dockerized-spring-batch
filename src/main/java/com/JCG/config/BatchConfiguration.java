package com.JCG.config;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.JCG.model.Data;
import com.JCG.model.DataRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	DataRepository dataRepository;

	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

	@Bean
	public FlatFileItemReader<Data> reader() {
		FlatFileItemReader<Data> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("sample-data.csv"));
		reader.setLinesToSkip(1);

		DefaultLineMapper<Data> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("id","firstName", "lastName","email","gender","ip_address");

		BeanWrapperFieldSetMapper<Data> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Data.class);

		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(tokenizer);
		reader.setLineMapper(lineMapper);

		return reader;
	}


	@Bean
	public JpaItemWriter<Data> writer() {
		JpaItemWriter<Data> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}

	@Bean
	public ItemProcessor<Data, Data> processor() {
		return (item) -> {
			return item;
		};
	}

	@Bean
	public Job importDataJob(JobExecutionListener listener) {
		return jobBuilderFactory.get("importDataJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1())
				.end()
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Data, Data>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListener() {


			@Override
			public void beforeJob(JobExecution jobExecution) {}

			@Override
			public void afterJob(JobExecution jobExecution) {
				if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
					log.info("Verification of the importing the import.");
					
					dataRepository.findAll().forEach(System.out::println);
					boolean isImportOK = dataRepository.findAll().size() == 1000;
					log.info("Does the number of elements in the DB matches the imported file ? " + isImportOK);
				}
			}
		};
	}
}

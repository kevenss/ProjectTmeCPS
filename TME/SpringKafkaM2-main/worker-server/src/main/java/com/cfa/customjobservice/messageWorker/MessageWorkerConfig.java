package com.cfa.customjobservice.messageWorker;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
@EnableBatchIntegration
@EnableBatchProcessing
public class MessageWorkerConfig {

    public static String TOPIC = "step-execution-eventslol";
    public static String GROUP_ID = "stepresponse_partition";

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ConsumerFactory kafkaFactory;
    @Autowired
    private RemoteChunkingWorkerBuilder workerBuilder;


    @Bean
    public IntegrationFlow workerFlow() {
        return this.workerBuilder
                .itemProcessor(new messageItemProcessor())
                .itemWriter(new messageItemWriter())
                .inputChannel(requests()) // requests received from the manager
                .outputChannel(replies()) // replies sent to the manager
                .build();
    }

    @Bean
    public DirectChannel requests(){ return new DirectChannel(); }

    @Bean
    public IntegrationFlow outboundFlow() {
        final KafkaProducerMessageHandler kafkaMessageHandler = new KafkaProducerMessageHandler(kafkaTemplate);
        kafkaMessageHandler.setTopicExpression(new LiteralExpression(MessageWorkerConfig.TOPIC));
        return IntegrationFlows
                .from(replies())
                .handle(kafkaMessageHandler)
                .get();
    }

    @Bean
    public QueueChannel replies() { return new QueueChannel(); }

    @Bean
    public IntegrationFlow inboundFlow () {
        final ContainerProperties containerProps = new ContainerProperties(MessageWorkerConfig.TOPIC);
        containerProps.setGroupId(MessageWorkerConfig.GROUP_ID);

        final KafkaMessageListenerContainer container = new KafkaMessageListenerContainer(kafkaFactory, containerProps);
        final KafkaMessageDrivenChannelAdapter kafkaMessageChannel = new KafkaMessageDrivenChannelAdapter(container);

        return IntegrationFlows
                .from(kafkaMessageChannel)
                .channel(requests())
                .get();
    }

    }

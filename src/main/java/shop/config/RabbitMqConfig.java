package shop.config;


import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

	@Value("${rabbitmq.exchange.email-exchange}")
	private String emailExchangeName;
	
	//Contact reply params
	@Value("${rabbitmq.queue.contact-reply}")
	private String contactReplyQueueName;
	@Value("${rabbitmq.routing-key.contact-reply}")
	private String contactReplyRoutingKey;
	
	//Registration confirmation params
	@Value("${rabbitmq.queue.registration-confirmation}")
	private String registrationConfirmationQueueName;
	@Value("${rabbitmq.routing-key.registration-confirmation}")
	private String registrationConfirmationRoutingKey;
	
	//Password reset params
	@Value("${rabbitmq.queue.password-reset}")
	private String passwordResetQueueName;
	@Value("${rabbitmq.routing-key.password-reset}")
	private String passwordResetRoutingKey;
	
	@Bean
	Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
	    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
	    
	    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
	    typeMapper.setTrustedPackages("shop.messaging.email.dto");
	    
	    converter.setJavaTypeMapper(typeMapper);
	    return converter;
	}
	
	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter);
		return template;
	}
	
	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
		var factory = new SimpleRabbitListenerContainerFactory();
		
		factory.setConnectionFactory(connectionFactory);
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		factory.setConcurrentConsumers(2);
		factory.setMaxConcurrentConsumers(10);
		factory.setPrefetchCount(1);
		factory.setDefaultRequeueRejected(false);
		factory.setMessageConverter(messageConverter);
		
		return factory;
	}
	
	@Bean
	DirectExchange emailDirectExchange() {
		return new DirectExchange(emailExchangeName, true, false);
	}
	
	//Email contacts beans
	@Bean
	Queue emailContactReplyQueue() {
		return new Queue(contactReplyQueueName, true, false, false);
	}
	@Bean
	Binding contactReplyBinding(DirectExchange emailDirectExchange, Queue emailContactReplyQueue) {
		return BindingBuilder.bind(emailContactReplyQueue).to(emailDirectExchange).with(contactReplyRoutingKey);
	}
	
	//Email confirmation beans
	@Bean 
	Queue registrationConfirmationQueue(){
		return new Queue(registrationConfirmationQueueName, true, false, false);
	}
	@Bean
	Binding registrationConfirmationBinding(DirectExchange emailDirectExchange, Queue registrationConfirmationQueue) {
		return BindingBuilder.bind(registrationConfirmationQueue).to(emailDirectExchange).with(registrationConfirmationRoutingKey);
	}

	//Password reset beans
	@Bean
	Queue emailPasswordResetQueue() {
		return new Queue(passwordResetQueueName, true, false, false);
	}
	@Bean
	Binding passwordResetBinding(DirectExchange emailDirectExchange, Queue emailPasswordResetQueue) {
		return BindingBuilder.bind(emailPasswordResetQueue).to(emailDirectExchange).with(passwordResetRoutingKey);
	}
	
	
}

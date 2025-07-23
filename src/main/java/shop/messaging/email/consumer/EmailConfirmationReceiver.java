package shop.messaging.email.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.exceptions.EmailSendingException;
import shop.messaging.email.dto.EmailConfirmationPayload;
import shop.services.email.EmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmationReceiver {
	
	private final EmailService emailService;
	
	@RabbitListener(queues="${rabbitmq.queue.registration-confirmation}", containerFactory = "rabbitListenerContainerFactory")
	public void processConfirmationPayload(Channel channel, Message amqpMessage, EmailConfirmationPayload confirmationPayload) {
		long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();
		try {
			emailService.sendVerificationEmail(confirmationPayload.getToEmail(), confirmationPayload.getConfirmationToken());
			channel.basicAck(deliveryTag, false);
		}catch (EmailSendingException e) {
			log.error("Could not send confirmation email.", e);
			try {
				channel.basicNack(deliveryTag, false, false);
			}catch(IOException e1){
				log.error("Error on nacking confirmation payload.", e1);
			}
		}catch(IOException e) {
			log.error("Error on acknowleding confirmation payload.", e);
		}
	}
}

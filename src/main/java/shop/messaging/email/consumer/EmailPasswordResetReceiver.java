package shop.messaging.email.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.exceptions.EmailSendingException;
import shop.messaging.email.dto.EmailPasswordResetPayload;
import shop.services.email.EmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailPasswordResetReceiver {
	
	private final EmailService emailService;
	
	@RabbitListener(queues = "${rabbitmq.queue.password-reset}", containerFactory = "rabbitListenerContainerFactory")
	public void processEmailReply(Channel channel, Message amqpMessage, EmailPasswordResetPayload passwordResetPayload) {
		long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();
		try {
			emailService.sendPasswordChangeLink(passwordResetPayload.getToEmail(), passwordResetPayload.getPasswordResetToken());
			channel.basicAck(deliveryTag, false);
		}catch(EmailSendingException e) {
			log.error("Could not send reply email.", e);
			try {
				channel.basicNack(deliveryTag, false, false);
			} catch (IOException e1) {
				log.error("Error on nacking email reply message.", e1);
			}
		} catch (IOException e) {
			log.error("Error on acknowleding email reply message.", e);
			
		}
	}
}

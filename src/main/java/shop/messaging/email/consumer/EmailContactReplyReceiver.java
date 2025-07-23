package shop.messaging.email.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.exceptions.EmailSendingException;
import shop.messaging.email.dto.EmailContactReplyPayload;
import shop.services.email.EmailService;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailContactReplyReceiver {
	
	private final EmailService emailService;
	
	@RabbitListener(queues = "${rabbitmq.queue.contact-reply}", containerFactory = "rabbitListenerContainerFactory")
	public void processEmailReply(Channel channel, Message amqpMessage, EmailContactReplyPayload replyMessage) {
		long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();
		try {
			emailService.sendContactReply(replyMessage.getToEmail(), replyMessage.getSubject(), replyMessage.getMessage());
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

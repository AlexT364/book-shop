package shop.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import shop.dto.book.BookDto;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {
	
	@Bean
	LettuceConnectionFactory redisConnectionFactory(
			@Value("${spring.redis.host}") String host,
			@Value("${spring.redis.port}") int port) {
		return new LettuceConnectionFactory(host, port);
	}
	
	@Bean
	RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.disableCachingNullValues()
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(
								new GenericJackson2JsonRedisSerializer()
								)
						);
		
		Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
		cacheConfigs.put("authors", config.entryTtl(Duration.ofHours(1)));
		
		
		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(config)
				.withInitialCacheConfigurations(cacheConfigs)
				.build();
	}
	
	@Bean
	@Qualifier("bookDtoRedisTemplate")
	RedisTemplate<String, BookDto> bookDtoRedisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String, BookDto> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		Jackson2JsonRedisSerializer<BookDto> jsonSerializer = new Jackson2JsonRedisSerializer<BookDto>(mapper, BookDto.class);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(jsonSerializer);
		
		return template;
	}
	
	@Bean
	@Qualifier("doubleRedisTemplate")
	RedisTemplate<String, Double> doubleRedisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String, Double> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		
		Jackson2JsonRedisSerializer<Double> jsonSerializer = new Jackson2JsonRedisSerializer<Double>(mapper, Double.class);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(jsonSerializer);
		
		return template;
		
	}
	
}













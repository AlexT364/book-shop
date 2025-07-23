package shop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shop.mapping.converters.RegisterUserRequestToUserConverter;
import shop.mapping.converters.book.BookReviewToDtoConverter;
import shop.mapping.converters.book.BookToCreateEditBookDtoConverter;
import shop.mapping.converters.book.BookToDtoConverter;
import shop.mapping.converters.book.BookToShortDtoConverter;
import shop.mapping.converters.cart.CartItemToOrderDetailsConverter;
import shop.mapping.converters.cart.CartPropertyMap;
import shop.mapping.converters.favourite.FavouriteToShortDtoConverter;
import shop.mapping.converters.order.CheckoutFormDtoToOrder;
import shop.mapping.converters.order.OrderDetailsToDtoConverter;
import shop.mapping.converters.order.OrderToDtoConverter;

@Configuration
public class MappingConfig {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new CartPropertyMap());
		modelMapper.addConverter(new OrderToDtoConverter());
		modelMapper.addConverter(new CartItemToOrderDetailsConverter());
		modelMapper.addConverter(new OrderDetailsToDtoConverter());
		modelMapper.addConverter(new FavouriteToShortDtoConverter());
		modelMapper.addConverter(new RegisterUserRequestToUserConverter());
		modelMapper.addConverter(new BookReviewToDtoConverter());
		modelMapper.addConverter(new CheckoutFormDtoToOrder());
		
		modelMapper.addConverter(new BookToDtoConverter());
		modelMapper.addConverter(new BookToShortDtoConverter());
		modelMapper.addConverter(new BookToCreateEditBookDtoConverter());
		
		return modelMapper;
	}

}

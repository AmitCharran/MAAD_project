package com.revature.maadcars.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class MaadCarsModelMapper {
	@Bean
	public static ModelMapper modelMapper() {
		return new ModelMapper();
	}
}

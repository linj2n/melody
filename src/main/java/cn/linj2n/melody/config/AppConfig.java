package cn.linj2n.melody.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // beans

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}

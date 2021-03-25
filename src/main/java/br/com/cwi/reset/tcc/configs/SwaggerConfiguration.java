package br.com.cwi.reset.tcc.configs;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("br.com.cwi.reset.tcc"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
    }
    
	private ApiInfo apiInfo() {
		 return new ApiInfo(
			 "Sebosão devilvery",
			 "Esta API é utilizada para gerenciar o aplicativo de comidas mais sebosas do Brasil",
			 "Versão 1.0",
			 "https://10619-2.s.cdn12.com/rests/original/312_207192114.jpg",
			 new Contact("Wando", "https://github.com/cwi-reset/tcc-wando777", "wanderson.eq2011@gmail.com"),
			 "Não nos responsabilizamos por danos a sua saúde",
			 "https://i.pinimg.com/originals/da/9a/fc/da9afcde89bb7e6cdf53970b8467a189.jpg",
			 Collections.emptyList()
		);
	}
}
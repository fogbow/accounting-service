package cloud.fogbow.accs.api.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cloud.fogbow.accs.constants.ApiDocumentation;
import cloud.fogbow.accs.constants.SystemConstants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
	
	public static final String BASE_PACKAGE = "cloud.fogbow.accs";
	
	public static final Contact CONTACT = new Contact(
            cloud.fogbow.common.constants.ApiDocumentation.ApiInfo.CONTACT_NAME,
            cloud.fogbow.common.constants.ApiDocumentation.ApiInfo.CONTACT_URL,
            cloud.fogbow.common.constants.ApiDocumentation.ApiInfo.CONTACT_EMAIL);

    @Bean
    public Docket apiDetails() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        docket.select()
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(this.apiInfo().build());

        return docket;
    }

    private ApiInfoBuilder apiInfo() {
        String versionNumber = SystemConstants.API_VERSION_NUMBER;

        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();

        apiInfoBuilder.title(ApiDocumentation.ApiInfo.API_TITLE);
        apiInfoBuilder.description(ApiDocumentation.ApiInfo.API_DESCRIPTION);
        apiInfoBuilder.version(versionNumber);
        apiInfoBuilder.contact(CONTACT);

        return apiInfoBuilder;
    }

}

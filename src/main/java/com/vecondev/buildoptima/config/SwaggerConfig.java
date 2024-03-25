package com.vecondev.buildoptima.config;

import static com.vecondev.buildoptima.util.FileReader.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
    info =
        @Info(
            title = "BUILDOPTIMA API",
            version = "V1",
            description = "PROPERTY INFORMATION MANAGEMENT"))
@SecurityScheme(
    name = "api-security",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT")
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI methodArgumentNotValidDocumentation() {
    return new OpenAPI()
        .components(new Components()
                .addResponses("MethodArgumentNotValidResponse",
                    new ApiResponse().description("There is an invalid value in user input.")
                        .content(new Content().addMediaType(APPLICATION_JSON_VALUE,
                                    new MediaType().schema(new MapSchema()
                                            .addProperties("errorCode",
                                                new IntegerSchema().description("The error code")
                                                    .example(4121))
                                            .addProperties("status",
                                                new StringSchema()
                                                    .description("HTTP response status")
                                                    .example("BAD_REQUEST"))
                                            .addProperties("timestamp",
                                                new ObjectSchema()
                                                    .description("""
                                                                Timestamp showing when
                                                                the error occurred""")
                                                    .example(LocalDateTime.now()))
                                            .addProperties("message",
                                                new StringSchema().description("The error message")
                                                    .example("Invalid field in fetch request."))
                                            .addProperties("errors", new MapSchema()
                                                        .addProperties("name",
                                                            new StringSchema()
                                                                .example("""
                                                                        The length should be between
                                                                        2 and 20 characters!"""))
                                                        .addProperties("password",
                                                            new StringSchema()
                                                                .example("""
                                                                         Invalid password! The password
                                                                         should have 8 up to 32 characters
                                                                         at least one uppercase character,
                                                                         one lowercase character, one digit,
                                                                         one special symbol and no whitespaces!""")))))))
                .addRequestBodies(
                        "FetchUsersRequestExample",
                        new RequestBody()
                                .content(
                                        new Content()
                                                .addMediaType(
                                                        APPLICATION_JSON_VALUE,
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new MapSchema().example(readFromJson("docs/json/user-filter-sorting-example.json"))))))
                .addRequestBodies(
                    "FetchFaqCategoriesRequestExample",
                    new RequestBody()
                            .content(
                                    new Content()
                                            .addMediaType(
                                                    APPLICATION_JSON_VALUE,
                                                    new io.swagger.v3.oas.models.media.MediaType()
                                                            .schema(new MapSchema().example(readFromJson("docs/json/faq_category-filter-sorting-example.json"))))))
                .addRequestBodies(
                        "FetchFaqQuestionsRequestExample",
                        new RequestBody()
                                .content(
                                        new Content()
                                                .addMediaType(
                                                        APPLICATION_JSON_VALUE,
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new MapSchema().example(readFromJson("docs/json/faq_question-filter-sorting-example.json"))))))
                .addRequestBodies(
                        "FetchNewsRequestExample",
                        new RequestBody()
                                .content(
                                        new Content()
                                                .addMediaType(
                                                        APPLICATION_JSON_VALUE,
                                                        new io.swagger.v3.oas.models.media.MediaType()
                                                                .schema(new MapSchema().example(readFromJson("docs/json/news-filter-sorting-example.json")))))));
  }
}

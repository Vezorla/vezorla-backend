package ca.sait.vezorla.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
//
//@Configuration
//@EnableJdbcHttpSession
//public class HttpSessionConfig extends AbstractHttpSessionApplicationInitializer {
//
//    @Bean
//    public EmbeddedDatabase dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .addScript("org/springframework/session/jdbc/schema-mysql.sql").build();
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}

/**
 * Temporary config class to allow for the front end to interact with the backend
 * This is necessary as currently, the front and backends are on different domains, which causes the session bug
 * we have seen earlier (where the front end is unable to persist the session
 * and it continues to query the backend and thus creating a multiple new sessions)
 *
 * This class will be removed prior to deployment as the front and back will be on the same domain.
 * Hopefully
 */
@Configuration
public class HttpSessionConfig implements WebMvcConfigurer {
    @SuppressWarnings("deprecation")
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("PUT", "POST", "GET")
                        .allowedHeaders("*")
                        .exposedHeaders("Content-Type", "Expires")
                        .allowCredentials(true).maxAge(3600);
            }
        };
    }
}
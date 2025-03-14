package nl.fontys.s3.ticketmaster.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        CorsConfiguration webhookConfig = new CorsConfiguration();


        config.addAllowedOrigin("http://localhost:5173");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");


        webhookConfig.addAllowedOrigin("http://localhost:5173");
        webhookConfig.addAllowedMethod("POST");
        webhookConfig.addAllowedHeader("*");
        webhookConfig.addAllowedHeader("Stripe-Signature");
        webhookConfig.setAllowCredentials(false);


        source.registerCorsConfiguration("/api/payments/webhook", webhookConfig);
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
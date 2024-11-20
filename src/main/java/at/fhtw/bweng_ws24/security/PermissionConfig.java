package at.fhtw.bweng_ws24.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class PermissionConfig {
    private final AccessPermissionEvaluator accessPermissionEvaluator;

    public PermissionConfig(AccessPermissionEvaluator accessPermissionEvaluator) {
        this.accessPermissionEvaluator = accessPermissionEvaluator;
    }

    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(accessPermissionEvaluator);

        return expressionHandler;
    }
}

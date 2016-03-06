package org.jalfrezi.server;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        packages(this.getClass().getPackage().getName());
        register(LoggingFilter.class);
        register(JacksonFeature.class);
        property("contextConfig", new AnnotationConfigApplicationContext(this.getClass().getPackage().getName(), "org.jalfrezi.dao"));
    }
}

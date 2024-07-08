To apply another filter for paths that match /create/** but are not /create/quote/**, you can define
another custom filter for these routes. Here’s how you can achieve this:
1. Set Up Gateway Routes: Configure the routes in application.properties for simple
redirection, for the special route /create/quote/**, and for other /create/** paths.
2. Custom Filters: Implement custom filters for each specific route logic.
Step 1: Configure Routes in application.properties
Add the following configuration to your application.properties file:
spring.cloud.gateway.routes[0].id=simple-redirect
spring.cloud.gateway.routes[0].uri=http://new-base-path
spring.cloud.gateway.routes[0].predicates[0]=Path=/some/path/**
spring.cloud.gateway.routes[1].id=create-quote
spring.cloud.gateway.routes[1].uri=lb://your-middleware-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/create/quote/**
spring.cloud.gateway.routes[2].id=create-other
spring.cloud.gateway.routes[2].uri=lb://your-middleware-service
spring.cloud.gateway.routes[2].predicates[0]=Path=/create/**
spring.cloud.gateway.routes[2].filters[0]=RewritePath=/create/(?&lt;segment&gt;.*), /${segment}
Step 2: Implement Custom Filters
Custom Filter for /create/quote/**
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class CustomQuoteFilter extends AbstractGatewayFilterFactory&lt;CustomQuoteFilter.Config&gt; {
@Autowired
private WebClient.Builder webClientBuilder;
public CustomQuoteFilter() {
super(Config.class);
}
public static class Config {

// Put the configuration properties for your filter here
}
@Override
public GatewayFilter apply(Config config) {
return (exchange, chain) -&gt; {
String path = exchange.getRequest().getURI().getPath();
if (path.matches(&quot;/create/quote/.*&quot;)) {
return callExternalApi(exchange, chain);
}
return chain.filter(exchange);
};
}
private Mono&lt;Void&gt; callExternalApi(ServerWebExchange exchange, GatewayFilterChain chain) {
return webClientBuilder.build()
.get()
.uri(&quot;http://external-api/path&quot;)
.retrieve()
.bodyToMono(String.class)
.flatMap(response -&gt; {
// Use the response to call the second API
return webClientBuilder.build()
.get()
.uri(&quot;http://bcapp/path&quot;)
.retrieve()
.bodyToMono(String.class)
.flatMap(secondResponse -&gt; {
// Process the second API response and forward it to the client
exchange.getResponse().setStatusCode(HttpStatus.OK);
return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
.bufferFactory()
.wrap(secondResponse.getBytes())));
});
});
}
}
Custom Filter for Other /create/** Paths
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class CustomCreateFilter extends AbstractGatewayFilterFactory&lt;CustomCreateFilter.Config&gt; {
public CustomCreateFilter() {

super(Config.class);
}
public static class Config {
// Put the configuration properties for your filter here
}
@Override
public GatewayFilter apply(Config config) {
return (exchange, chain) -&gt; {
// Custom logic for other /create/** paths
return chain.filter(exchange);
};
}
}
Step 3: Register the Custom Filters
Register your custom filters in the Spring Boot application:
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GatewayConfig {
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder, CustomQuoteFilter
customQuoteFilter, CustomCreateFilter customCreateFilter) {
return builder.routes()
.route(&quot;create-quote&quot;, r -&gt; r.path(&quot;/create/quote/**&quot;)
.filters(f -&gt; f.filter(customQuoteFilter.apply(new CustomQuoteFilter.Config())))
.uri(&quot;lb://your-middleware-service&quot;))
.route(&quot;create-other&quot;, r -&gt; r.path(&quot;/create/**&quot;)
.filters(f -&gt; f.filter(customCreateFilter.apply(new CustomCreateFilter.Config())))
.uri(&quot;lb://your-middleware-service&quot;))
.build();
}
}
With this setup, the CustomQuoteFilter will handle requests to the /create/quote/** path, and the
CustomCreateFilter will handle other /create/** paths. The routes are configured in
application.properties, and the custom filters are applied through the route definitions in the
GatewayConfig class.

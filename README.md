@Slf4j
@Configuration
public class SmartRouteConfiguration {

	@Value("${smartapi.uri}")
	private String smartapiuri;

	@Value("${gatewayapi.uri}")
	private String gatewayUri;

	@Value("${smartapi.middleware.basePath}")
	private String gatewayapiBasePath;

	@Bean
	public RouteLocator smartRouting(RouteLocatorBuilder builder, SmartCreateQuoteRouteFilter createQuoteRouteFilter,
			ProducerOAuthFilter producerOAuthFilter, FetchQuoteRouteFilter fetchQuoteRoute,
			SmartRouteFilter smartRouteFilter, SmartOAuthFilter smartOAuthFilter) {
		log.info(" ******* SMART API RouteLocator smartRouting ******* ");
		log.debug(" Connecting... " + smartapiuri);
		return builder.routes()
				.route("smart-api-create-quote", route -> route.path(gatewayapiBasePath + "/submitquote/**")
						.filters(filter -> filter.filter(producerOAuthFilter).filter(fetchQuoteRoute)
								.filter(smartOAuthFilter).filter(createQuoteRouteFilter)
								.circuitBreaker(circuitBreaker -> circuitBreaker.setName("circuit-breaker-route")
										.setFallbackUri(URI.create("forward:/fallback"))))
						.uri(gatewayUri))
				.route("smart-api-route", route -> route.path(gatewayapiBasePath + "/**")
						.filters(filter -> filter.filter(smartOAuthFilter).filter(smartRouteFilter)
								.circuitBreaker(circuitBreaker -> circuitBreaker.setName("circuit-breaker-route")
										.setFallbackUri(URI.create("forward:/fallback"))))
						.uri(smartapiuri))
				.build();
	}
}



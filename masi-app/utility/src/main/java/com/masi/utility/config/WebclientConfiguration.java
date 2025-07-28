package com.masi.utility.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableDiscoveryClient
// cảm ơn  https://stackoverflow.com/questions/76473889/service-discovery-not-working-when-using-webclient-with-consul
public class WebclientConfiguration {

    private final ObjectMapper objectMapper;
    @Value("${web.client.read-timeout:25000}")
    private final int webClientReadTimeout;
    @Value("${web.client.connection-timeout:3000}")
    private final int webClientConnectionTimeout;

    public WebclientConfiguration(ObjectMapper objectMapper,
                                  @Value("${web.client.read-timeout:25000}") int webClientReadTimeout,
                                  @Value("${web.client.connection-timeout:3000}") int webClientConnectionTimeout) {
        this.objectMapper = objectMapper;
        this.webClientReadTimeout = webClientReadTimeout;
        this.webClientConnectionTimeout = webClientConnectionTimeout;
    }

    private ClientHttpConnector getClientHttpConnector() {
        return new ReactorClientHttpConnector(
            HttpClient.create().compress(true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientConnectionTimeout)
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(webClientReadTimeout, TimeUnit.MILLISECONDS))));
    }

    @Bean
    public DefaultUriBuilderFactory builderFactory() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return factory;
    }

    @Bean(name = "webClientConsul")
    public WebClient webClientConsul(
        WebClient.Builder webClientBuilder,
        DeferringLoadBalancerExchangeFilterFunction<LoadBalancedExchangeFilterFunction> exchangeFilterFunction
    ) {
        webClientBuilder.filter(exchangeFilterFunction);
        return buildWebClient(webClientBuilder);
    }

    @Bean(name = "webClientDefault")
    public WebClient webClientDefault(WebClient.Builder webClientBuilder) {
        return buildWebClient(webClientBuilder);
    }

    private WebClient buildWebClient(WebClient.Builder webClientBuilder) {
        ClientHttpConnector connector = getClientHttpConnector();
        return webClientBuilder
            .clientConnector(connector)
            .exchangeStrategies(getExchangeStrategies())
            .build();
    }

    private ExchangeStrategies getExchangeStrategies() {
        return  ExchangeStrategies.builder()
            .codecs(clientDefaultCodecsConfigurer -> {
                clientDefaultCodecsConfigurer
                    .defaultCodecs()
                    .jackson2JsonEncoder(
                        new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_NDJSON));
                clientDefaultCodecsConfigurer
                    .defaultCodecs()
                    .jackson2JsonDecoder(
                        new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_NDJSON));
            }).build();
    }

}


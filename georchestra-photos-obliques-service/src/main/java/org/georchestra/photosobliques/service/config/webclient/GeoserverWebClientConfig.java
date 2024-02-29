package org.georchestra.photosobliques.service.config.webclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.georchestra.photosobliques.service.helper.common.HttpClientHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

/**
 * @author FNI18300
 *
 */
@Component
public class GeoserverWebClientConfig extends AbstractWebClientConfig {

	@Getter
	@Value("${photos_obliques.geoserver.password}")
	private String geoserverPassword;

	@Getter
	@Value("${photos_obliques.geoserver.user}")
	private String geoserverUser;

	@Getter
	@Value("${photos_obliques.geoserver.host}")
	private String geoserverBaseUrl;

	@Value("${photos_obliques.geoserver.trust-all-certs:true}")
	private boolean trustAllCerts;

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Bean(name = "geoserver_webclient_builder")
	public WebClient.Builder webClientBuilder() throws SSLException {
		// On crée un nouvel opbjectMapper
		ObjectMapper objectMapper = createObjectMapper();

		// On modifie l'exchange strategie pour que ce object mapper soit utiliser pour la desieralization des json du Geoserver
		ExchangeStrategies strategies = createStrategies(objectMapper);
		HttpClient httpClient = httpClientHelper.createReactorHttpClient(trustAllCerts);
		return WebClient.builder()
				.exchangeStrategies(strategies)
				.baseUrl(geoserverBaseUrl)
				.defaultHeaders(header -> header.setBasicAuth(geoserverUser, geoserverPassword))
				.clientConnector(new ReactorClientHttpConnector(httpClient));
	}

	@Bean(name = "geoserver_webclient")
	public WebClient webClient(@Qualifier("geoserver_webclient_builder") WebClient.Builder builder) {
		return builder.build();
	}
}

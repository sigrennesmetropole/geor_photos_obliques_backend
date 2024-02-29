/**
 *
 */
package org.georchestra.photosobliques.service.helper.common;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProtocols;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author FNI18300
 *
 */
@Component
public class HttpClientHelper {

	/**
	 * Création d'un client Http
	 *
	 * @param trustAllCerts pour accepter les certificats autosignés
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public HttpClient createHttpClient(boolean trustAllCerts)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		if (trustAllCerts) {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy).build();

			clientBuilder.setSSLContext(sslContext);

			HostnameVerifier allHostsValid = (hostname, session) -> trustAllCerts;
			clientBuilder.setSSLHostnameVerifier(allHostsValid);
		}
		return clientBuilder.build();
	}

	public reactor.netty.http.client.HttpClient createReactorHttpClient(boolean trustAllCerts) throws SSLException {
		reactor.netty.http.client.HttpClient client = reactor.netty.http.client.HttpClient.create();

		if (trustAllCerts) {
			SslContextBuilder sslContextBuilder = SslContextBuilder.forClient().protocols(SslProtocols.TLS_v1_2);
			SslContext sslContext = sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE).build();

			client = client.secure(spec -> spec.sslContext(sslContext));
		}
		client.compress(true);

		return client;
	}
}

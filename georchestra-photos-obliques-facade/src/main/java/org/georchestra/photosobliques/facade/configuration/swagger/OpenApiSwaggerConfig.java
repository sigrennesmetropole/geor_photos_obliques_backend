package org.georchestra.photosobliques.facade.configuration.swagger;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author FNI18300
 *
 */
public class OpenApiSwaggerConfig {

	@Value("${swagger-server:"+
			"http://localhost:8082/"+
			"}")
	private List<String> serverUrls;
	@Bean
	public OpenAPI springOpenAPI() {
		OpenAPI openAPI = new OpenAPI().openapi("3.0.0").info(apiInfo()).components(apiComponents()).security(apiSecurityRequirements());

		if( CollectionUtils.isNotEmpty(serverUrls)) {
			openAPI.servers(computeServers());
		}
		return openAPI;
	}

	protected Info apiInfo() {
		return new Info().title("Georchestra - Plugin Photos Obliquees API").version("1.0");
	}

	protected Components apiComponents() {
		return new Components().addSecuritySchemes("basicauth", securityScheme());
	}
	protected io.swagger.v3.oas.models.security.SecurityScheme securityScheme() {
		return new io.swagger.v3.oas.models.security.SecurityScheme().type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP).scheme("basic");
	}

	protected List<SecurityRequirement> apiSecurityRequirements() {
		return Collections.singletonList(new SecurityRequirement().addList("basicauth"));
	}

	protected List<Server> computeServers() {
		List<Server> result = new ArrayList<>();
		for (String serverUrl : serverUrls) {
			Server server = new Server();
			server.setUrl(serverUrl);
			result.add(server);
		}
		return result;
	}
}

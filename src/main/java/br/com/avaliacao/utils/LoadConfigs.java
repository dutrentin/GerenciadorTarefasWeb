package br.com.avaliacao.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

public class LoadConfigs {
	
	public static Client client;
	private static String urlApiRest;
	private static WebTarget target;
	
	
	public static WebTarget loadConfigs(){
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		client = ClientBuilder.newClient(config);
		urlApiRest = UtilResource.getResourceProperty("config", "hotname_api_restful");
		return target = client.target(urlApiRest);
	}
	
	public static String getHostPath() {
		return UtilResource.getResourceProperty("config", "hotname_api_restful");
	}

}

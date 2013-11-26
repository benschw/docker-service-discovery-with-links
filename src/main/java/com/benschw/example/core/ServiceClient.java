package com.benschw.example.core;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ServiceClient {

	private Client client;
	private String host;
	private int port;

	public ServiceClient(Client client, String host, int port) {
		this.client = client;
		this.host = host;
		this.port = port;
	}


	public WebResource getResource() {

		return client.resource("http://" + host + ":" + port);
	}


}

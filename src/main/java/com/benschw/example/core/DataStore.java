package com.benschw.example.core;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
	private String hostName;
	private int portNum;
	private MemcachedClient client;

	private static final String KEY = "foo";

	public DataStore(String hostName, int portNum) {
		this.hostName = hostName;
		this.portNum = portNum;
	}

	private MemcachedClient getClient() {
		if (client == null) {
			try {
				client = new MemcachedClient(new InetSocketAddress(hostName, portNum));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return client;
	}

	public void push(String value) {
		List<String> l = getList();
		l.add(value);


		getClient().set(KEY, 3600, l);
	}

	public List<String> getList() {
		List<String> l =  (List<String>) getClient().get(KEY);
		return l == null ? new ArrayList<String>() : l;
	}
}
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
		ArrayList<String> l = getList();
		l.add(value);


		getClient().set(KEY, 3600, l);
	}

	public ArrayList<String> getList() {
		try {
			ArrayList<String> l =  (ArrayList<String>) getClient().get(KEY);
			return l == null ? new ArrayList<String>() : l;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
}

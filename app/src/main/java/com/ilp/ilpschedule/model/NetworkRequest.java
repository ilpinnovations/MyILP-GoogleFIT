package com.ilp.ilpschedule.model;

import java.util.Map;

public class NetworkRequest {
	@Override
	public String toString() {
		return "NetworkRequest [Url=" + Url + ", parameters=" + parameters
				+ "]";
	}

	private String Url;
	private Map<String, Object> parameters;

	public NetworkRequest(String url, Map<String, Object> parameters) {
		super();
		Url = url;
		this.parameters = parameters;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}

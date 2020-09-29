package com.subham.springfirst.model;

import java.util.HashMap;
import java.util.Map;

public class CustomError {
	@SuppressWarnings("unused")
	private final String service_name = "Demo service";
	@SuppressWarnings("unused")
	private final String developer_name = "Subham Santra";
	private final Map<String, String> _links = new HashMap<>();
	{
		_links.put("person_service", "localhost:8080/persons");
		_links.put("post_service", "localhost:8080/persons");
	}
	public CustomError() {}
	
}

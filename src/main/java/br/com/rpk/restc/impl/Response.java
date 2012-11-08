package br.com.rpk.restc.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.rpk.restc.Resource;

public class Response implements br.com.rpk.restc.Response {

	private HttpResponse httpResponse;
	private String rawContent;
	
	public Response(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
		consumesContent(); // consumes body response
	}

	@Override
	public String asRaw() {
		return this.rawContent;
	}
	
	@Override
	public JSONObject asJSONObject() {
		if (!isJsonResponse()) {
			throw new RuntimeException("Response is not a JSON string!");
		}
		try { return new JSONObject(rawContent); } 
		catch (JSONException e) {
			throw new RuntimeException("Not a valid JSONObject", e);
		}
	}
	
	@Override
	public JSONArray asJSONArray() {
		if (!isJsonResponse()) {
			throw new RuntimeException("Response is not a JSON string!");
		}
		try { return new JSONArray(rawContent); } 
		catch (JSONException e) {
			throw new RuntimeException("Not a valid JSONArray", e);
		}
	}

	private boolean isJsonResponse() {
		return httpResponse.getHeaders("Content-Type")[0].getValue().contains("application/json") ||
			   httpResponse.getHeaders("Content-Type")[0].getValue().contains("text/javascript");
	}

	public Response consumesContent() {
		try { 
			if (responseHasBodyContent()) {
				this.rawContent = EntityUtils.toString(httpResponse.getEntity()); 
			}
		} 
		catch (IOException e) { throw new RuntimeException("Cannot parse WS response", e); }
		return this;
	}
	
	private boolean responseHasBodyContent() {
		return httpResponse.getEntity() != null;
	}

	@Override
	public String getStatus() {
		return httpResponse.getStatusLine().toString();
	}

	@Override
	public int getCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}

	@Override
	public Resource asResource() {
		if (isJsonResponse()) {
			if (isRootObject()) {
				return new JSONResource(asJSONObject());
			} else if (isRootArray()) {
				return new JSONResource(asJSONArray());
			}
		} else if (isXmlResonse()) {
			return new XMLResource(rawContent);
		}
		return null;
	}

	private boolean isXmlResonse() {
		return httpResponse.getHeaders("Content-Type")[0].getValue().contains("application/xml") ||
		   	   httpResponse.getHeaders("Content-Type")[0].getValue().contains("text/xml");
	}

	private boolean isRootObject() {
		return this.rawContent.startsWith("{");
	}
	
	private boolean isRootArray() {
		return this.rawContent.startsWith("[");
	}
}
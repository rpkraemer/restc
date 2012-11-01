package br.com.rpk.restc.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import br.com.rpk.restc.MediaType;
import br.com.rpk.restc.Resource;
import br.com.rpk.restc.Response;

public class Request implements br.com.rpk.restc.Request {

	private String uri;
	private StringEntity data;
	private DefaultHttpClient client;
	private MediaType mediaType = MediaType.FORM_ENCODED; // default
	
	public Request(String uri) {
		this.uri = uri;
		newDefaultHttpClient();
	}
	
	public Request(URI uri) {
		this.uri = uri.toString();
		newDefaultHttpClient();
	}

	private void newDefaultHttpClient() {
		this.client = new DefaultHttpClient();
	}
	
	private boolean hasDataToSend() {
		return this.data != null;
	}
	
	/**
	 * Set the <strong>data</strong> content as JSON or XML payload
	 * @param data
	 * @return
	 */
	private br.com.rpk.restc.Request jsonOrXml(String data) {
		try {
			this.data = new StringEntity(data);
			this.data.setContentType(mediaType.getMediaType());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Data passed is invalid", e);
		}
		return this;
	}
	
	/**
	 * Set the <strong>data</strong> content as Form Encoded payload
	 * @param keyValue
	 * @return
	 */
	private br.com.rpk.restc.Request formEncoded(String ...keyValue) {
		if (keyValue.length == 0) {
			throw new IllegalArgumentException("Must pass data to send to server.");
		}
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		for (String kv : keyValue) {
			String key = kv.split("=>")[0].trim();
			String value = kv.split("=>")[1].trim();
			data.add(new BasicNameValuePair(key, value));
		}
		this.data = encode(data);
		return this;
	}
	
	@Override
	public br.com.rpk.restc.Request data(List<String> keyValues) {
		data(keyValues.toArray(new String[]{})); // alternative to varargs
		return this;
	}
	
	private StringEntity encode(List<NameValuePair> keyValuePairs) {
		try {
			UrlEncodedFormEntity data = new UrlEncodedFormEntity(keyValuePairs, "UTF-8");
			return data;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Data passed is invalid", e);
		}
	}

	@Override
	public Response get() {
		HttpGet get = new HttpGet(uri);
		try { 
			HttpResponse httpResponse = client.execute(get);
			Response response = new br.com.rpk.restc.impl.Response(httpResponse);
			get.releaseConnection();
			return response;
		} 
		catch (Exception e) { throw new RuntimeException("Cannot invoke WS (GET)", e); }
	}
	
	@Override
	public Response post() {
		try {
			HttpPost post = new HttpPost(uri);
			if (hasDataToSend()) {
				post.setEntity(data);
			}
			HttpResponse httpReponse = client.execute(post);
			Response response = new br.com.rpk.restc.impl.Response(httpReponse);
			post.releaseConnection();
			return response;
		} 
		catch (Exception e) { throw new RuntimeException("Cannot invoke WS (POST)", e); }
	}
	
	@Override
	public Response put() {
		try {
			HttpPut put = new HttpPut(uri);
			if (hasDataToSend()) {
				put.setEntity(data);
			}
			HttpResponse httpResponse = client.execute(put);
			Response response = new br.com.rpk.restc.impl.Response(httpResponse);
			put.releaseConnection();
			return response;
		}
		catch (Exception e) { throw new RuntimeException("Cannot invoke WS (PUT)", e); }
	}
	
	@Override
	public Response delete() {
		try {
			HttpUriRequest delete;
			if (!hasDataToSend()) {
				delete = new HttpDelete(uri);
			} else {
				HttpDeleteWithBody deleteWithBody = new HttpDeleteWithBody(uri);
				deleteWithBody.setEntity(data);
				delete = deleteWithBody;
			}
			HttpResponse httpResponse = client.execute(delete);
			Response response = new br.com.rpk.restc.impl.Response(httpResponse);
			return response;
		} 
		catch (Exception e) { throw new RuntimeException("Cannot invoke WS (DELETE)", e); }
	}

	@Override
	public br.com.rpk.restc.Request mediaType(MediaType mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	@Override
	public br.com.rpk.restc.Request data(String ...data) {
		switch (mediaType) {
			case FORM_ENCODED: formEncoded(data); break;
			case JSON: jsonOrXml(data[0]); break;
			case XML: jsonOrXml(data[0]); break;
		}
		return this;
	}

	@Override
	public br.com.rpk.restc.Request resource(Resource resource) {
		if (resource instanceof JSONResource) {
			mediaType = MediaType.JSON;
			jsonOrXml(resource.toJSON());
		}
		return this;
	}
}
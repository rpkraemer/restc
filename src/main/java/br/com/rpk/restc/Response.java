package br.com.rpk.restc;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 * Response </br></br>
 * 
 * Encapsulates the server HTTP response. <br/>
 * Provide methods to handle with response body and verify requistion status
 * 
 * @author Robson Kraemer (rpkraemer@gmail.com)
 *
 */
public interface Response {

	/**
	 * 
	 * @return the http response code
	 */
	int getCode();
	
	/**
	 * 
	 * @return the http response status
	 */
	String getStatus();
	
	/**
	 * 
	 * @return the http response body as raw content
	 */
	String asRaw();
	
	/**
	 * 
	 * @return the http response body as JSONObject content
	 */
	JSONObject asJSONObject();
	
	/**
	 * 
	 * @return the http response body as JSONArray content
	 */
	JSONArray asJSONArray();
	
	/**
	 * 
	 * @return the http response body as a Resource.<br/>
	 * Resource is an abstraction to provide a unique interface to
	 * operate on JSON and XML(not yet) content 
	 */
	Resource asResource();
}

package br.com.rpk.restc;

import java.util.List;

/**
 * Request </br></br>
 * 
 * A Request object is responsible for HTTP operations on remote resources/services. <br/>
 * Its the basis of Restc.
 * 
 * @author Robson Kraemer
 *
 */
public interface Request {

	/**
	 * Makes a HTTP GET request to the specified uri.
	 * @return self
	 */
	Response get();
	
	/**
	 * Makes a HTTP POST request to the specified uri.
	 * @return self
	 */
	Response post();
	
	/**
	 * Makes a HTTP PUT request to the specified uri.
	 * @return self
	 */
	Response put();
	
	/**
	 * Makes a HTTP DELETE request to the specified uri.
	 * @return self
	 */
	Response delete();
	
	/**
	 * Media Type of requisition payload
	 * @param mediaType
	 * @return self
	 */
	Request mediaType(MediaType mediaType);
	
	/**
	 * Data (MediaType specific) to send to server
	 * @param data
	 * @return
	 */
	Request data(String ...data);
	
	/**
	 * Data (MediaType specific) to send to server. <br/>
	 * Overload of data(String ...data)
	 * @param data
	 * @return
	 */
	Request data(List<String> data);
	
	/**
	 * Resource to send to server
	 * @param resource
	 * @return
	 */
	Request resource(Resource resource);
}

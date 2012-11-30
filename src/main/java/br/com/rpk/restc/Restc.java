package br.com.rpk.restc;

import java.net.URI;

/**
 * Restc </br></br>
 * 
 * Entry point to use the Restc project
 * @author Robson Kraemer (rpkraemer@gmail.com)
 *
 */
public class Restc {
	
	/**
	 * Entry point of client
	 * @param uri the rest resource/service
	 */
	static public Request at(String uri) {
		return new br.com.rpk.restc.impl.Request(uri); 
	}
	
	/**
	 * Entry point of client
	 * @param uri the rest resource/service
	 */
	static public Request at(URI uri) {
		return new br.com.rpk.restc.impl.Request(uri); 
	}
	
}
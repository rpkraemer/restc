package br.com.rpk.restc;

/**
 * Represents media types
 * @author Robson Kraemer
 *
 */
public enum MediaType {
	
	JSON		 ("application/json"), 
	XML			 ("application/xml"), 
	FORM_ENCODED ("application/x-www-form-urlencoded");
	
	private String mediaType;
	
	private MediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getMediaType() {
		return this.mediaType;
	}
	
}

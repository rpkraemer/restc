package br.com.rpk.restc;

/**
 * Resource </br></br>
 * 
 * Its a layer of abstraction to work with payloads and responses from server with a uniform interface. </br>
 * It provides a single API to operate on JSON and XML resources. </br>
 * Doesn't require to have stubs, dtos, proxies or any kind of this, its's independent of server model.</br></br>
 * 
 * <b>Presently, just JSON support has been implemented</b>
 * @author Robson Kraemer (rpkraemer@gmail.com)
 *
 */
public interface Resource {

	/**
	 * Get from current resource the given <b>property</b>
	 * @param property
	 * @return the property value
	 */
	Object get(String property);
	
	/**
	 * Get from current resource's child at <b>index</b> the given <b>property</b>
	 * @param index
	 * @param the property
	 * @return the property value
	 */
	Object get(int index, String property);
	
	/**
	 * Get from current resource's child or property at <b>index</b>
	 * @param index
	 * @return the property value
	 */
	Object get(int index);
	
	/**
	 * Same as {@link #get(String)}, but return directly a Resource instead Object. 
	 * Eliminates the Cast requirement <br/>
	 * Can be used when a get operation will return another object structure instead a single/atomic value.
	 * @param property
	 * @return the property value
	 */
	Resource getAsResource(String property);
	
	/**
	 * Same as {@link #get(int, String)}, but return directly a Resource instead Object. 
	 * Eliminates the Cast requirement <br/>
	 * Can be used when a get operation will return another object structure instead a single/atomic value.
	 * @param index
	 * @param property
	 * @return self
	 */
	Resource getAsResource(int index, String property);
	
	/**
	 * Same as {@link #get(int)}, but return directly a Resource instead Object. 
	 * Eliminates the Cast requirement <br/>
	 * Can be used when a get operation will return another object structure instead a single/atomic value.
	 * @param index
	 * @return self
	 */
	Resource getAsResource(int index);
	
	/**
	 * Set in current object <b>property</b> the given <b>value</b>
	 * @param property
	 * @param value
	 */
	void set(String property, Object value);
	
	/**
	 * Set in current object child at <b>index</b> the given <b>value</b> at <b>property</b>
	 * @param index
	 * @param property
	 * @param value
	 */
	void set(int index, String property, Object value);
	
	/**
	 * Set in current object child at <b>index</b> the given <b>value</b>
	 * @param index
	 * @param value
	 */
	void set(int index, Object value);

	/**
	 * Only return content when server response is JSON.
	 * @return json representation of current resource
	 */
	String toJSON();
}
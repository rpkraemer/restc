package br.com.rpk.restc.impl;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpk.restc.Resource;
import br.com.rpk.restc.impl.JSONResource;

public class JSONResourceTest {

	private Resource jsonObject;
	
	@Before
	public void setUp() throws JSONException {
		jsonObject = new JSONResource(givenValidJSONObject());
	}

	private JSONObject givenValidJSONObject() throws JSONException {
		return new JSONObject("{'_links': {'self': { 'href': '/orders' }, " +
							  "'next': { 'href': '/orders?page=2' }, " +
							  "'find': {'href': '/orders{?id}', 'templated': true }, " +
							  "'admin': [{ 'href': '/admins/2', 'title': 'Fred'}, {'href': '/admins/5', 'title': 'Kate' }]}," +
							  "currentlyProcessing: 14, shippedToday: 20, stringProp: 'Hello from json'," +
							  "'_embedded': {'orders': [{'_links': {'self': { 'href': '/orders/123' }," +
							  "'basket': { 'href': '/baskets/98712' }," +
							  "'customer': { 'href': '/customers/7809' }},'total': 30.00," +
							  "'currency': 'USD', 'status': 'shipped',}, " +
							  "{'_links': {'self': { 'href': '/orders/124' }," +
							  			  "'basket': { 'href': '/baskets/97213' }," +
							  			  "'customer': { 'href': '/customers/12369' }}," +
							  "'total': 20.00," +
							  "'currency': 'USD'," +
							  "'status': 'processing'}]}}");
	}
	
	@Test
	public void shouldRecuperateAnIntegerProperty() {
		int property = (Integer) jsonObject.get("shippedToday");
		Assert.assertEquals(20, property);
	}
	
	@Test
	public void shouldRecuperateANestedStringProperty() {
		Resource embedded = (Resource) jsonObject.get("_embedded");
		Resource orders = (Resource) embedded.get("orders");
		Resource links = (Resource) orders.get(0, "_links");
		Resource self = (Resource) links.get("self");
		Assert.assertEquals("/orders/123", self.get("href"));
	}
	
	@Test
	public void shouldRecuperateANestedDoubleProperty() {
		double total = (Double) jsonObject
			.getAsResource("_embedded").getAsResource("orders")
			.getAsResource(0).get("total");
		Assert.assertEquals(30.0, total, 0);
	
	}
	
	@Test
	public void shouldRecuperateAStringProperty() {
		String property = (String) jsonObject.get("stringProp");
		Assert.assertEquals("Hello from json", property);
	}
	
	@Test
	public void shouldUpdateAStringProperty() {
		jsonObject.set("stringProp", "updated");
		String property = (String) jsonObject.get("stringProp");
		Assert.assertEquals("updated", property);
	}
	
	@Test
	public void shouldUpdateAnIntegerProperty() {
		jsonObject.set("currentlyProcessing", 15);
		int property = (Integer) jsonObject.get("currentlyProcessing");
		Assert.assertEquals(15, property);
	}
	
	@Test
	public void shouldUpdateANestedDoubleProperty() {
		jsonObject
		.getAsResource("_embedded").getAsResource("orders")
		.getAsResource(0).set("total", 32.45);
		
		double total = (Double) jsonObject
		.getAsResource("_embedded").getAsResource("orders")
		.getAsResource(0).get("total");
		Assert.assertEquals(32.45, total, 0);
	}
}
package br.com.rpk.restc.impl;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import br.com.rpk.restc.Resource;

public class JSONResource implements Resource {

	private JSONObject object;
	private JSONArray array;
	
	JSONResource(JSONObject object) {
		this.object = object;
	}
	
	JSONResource(JSONArray array) {
		this.array = array;
	}

	@Override
	public Object get(String property) {
		if (object != null) {
			try {
				Object value = object.get(property);
				return correct(value);
			} catch (JSONException e) {
				throw new RuntimeException("Not a valid JSONObject", e);
			}
		} else {
			return "";
		}
	}

	@Override
	public void set(String property, Object value) {
		if (object != null) {
			try {
				object.put(property, value);
			} catch (JSONException e) {
				throw new RuntimeException("Not a valid JSONObject", e);
			}
		}
	}

	@Override
	public Object get(int index, String property) {
		if (array != null) {
			try { 
				Object value = array.getJSONObject(index).get(property);
				return correct(value);
			} catch (JSONException e) {
				throw new RuntimeException("Not a valid JSONObject", e);
			}
		} else {
			return "";
		}
	}

	@Override
	public void set(int index, String property, Object value) {
		if (array != null) {
			try { array.getJSONObject(index).put(property, value); }
			catch (JSONException e) {
				throw new RuntimeException("Not a valid JSONObject", e);
			}
		}
	}

	@Override
	public Object get(int index) {
		if (array != null) {
			try { 
				Object value = array.get(index);
				return correct(value);
			} catch (JSONException e) {
				throw new RuntimeException("Not a valid JSONObject", e);
			}
		} else {
			return "";
		}
	}

	/**
	 * Return a JSONInstance or Object according with <b>value</b> type
	 * @param value
	 * @return resource instance or object instance
	 */
	private Object correct(Object value) {
		if (JSONArray.class == value.getClass()) {
			return new JSONResource((JSONArray) value); 
		} else if (JSONObject.class == value.getClass()) {
			return new JSONResource((JSONObject) value);
		} else {
			return value;
		}
	}

	@Override
	public Resource getAsResource(String property) {
		return (Resource) get(property);
	}

	@Override
	public Resource getAsResource(int index, String property) {
		return (Resource) get(index, property);
	}

	@Override
	public Resource getAsResource(int index) {
		return (Resource) get(index);
	}

	@Override
	public void set(int index, Object value) {
		if (array != null) {
			try { array.put(index, value); }
			catch (JSONException e) {
				throw new RuntimeException("Not a valid JSONObject", e);
			}
		}
	}

	@Override
	public String toJSON() {
		String json = "";
		if (object != null) {
			json = object.toString();
		} else if (array != null) {
			json = array.toString();
		}
		return json;
	}
}

# Restc

REST client for consume resources/services without DTOs or Proxies

## examples

### HTTP GET

Let's see how to do a GET request on a remote resource:
    
    String content = Restc.at("http://freegeoip.net/json/74.125.234.31").get().asRaw();
    System.out.println(content); // json string

Really simple, in above example we made a request to freegeoip.net API and returned content in raw manner.

Let's increment a little, now we want to treat the response as a Java Object, to make interation easily:

    Resource object = Restc.at("http://freegeoip.net/json/74.125.234.31").get().asResource();
    System.out.println(object.get("city")); // Mountain View

Nice! 

In above code, we change the return to Resource (asResource()), Resource is an abstraction to read and modify
content returned from server as a Java object. In above example, we get the property "city" from the returned JSON.

### HTTP POST

What about post some data to server?

Let's do it, by creating a new Entry in a remote Rails application:

    Response r = Restc.at("http://afternoon-fortress-4074.herokuapp.com/entries")
	    .data("entry[description] => Hello from Restc!",
		      "entry[value] => -123.098",
		      "entry[entry_type] => Some type",
		      "entry[when] => 2012-11-01")
	    .post();

    System.out.println(r.getStatus()); // HTTP/1.1 302 Found

What did we? We did a POST to the URI specified passing the data as FORM ENCODED (default Media Type).
Restc currently supports form encoded, json and xml media types.

### HTTP PUT

Let's update the resource created before.

First, we need to get it from Rails app:

    Request req = Restc.at("http://afternoon-fortress-4074.herokuapp.com/entries/52.json");
    Resource entry = req.get().asResource();

Above, we make a Request object to entry URI and executed GET on it;
Now, let's update the property we want and PUT the modifications to Rails app:

    entry.set("description", "new entry description");
    req.resource(entry).put();

There are other manners to do the same thing:

    entry.set("value", 10);
    String json = entry.toJSON();
    req.mediaType(MediaType.JSON).data(json).put();

### HTTP DELETE

Finally, we want to delete the resource and finish this README for now :P:

    Restc.at("http://afternoon-fortress-4074.herokuapp.com/entries/52").delete();

## license
Restc is licensed under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
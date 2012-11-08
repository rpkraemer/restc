package br.com.rpk.restc.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpk.restc.Resource;

public class XMLResourceTest {

    private Resource xml;
    
    @Before
    public void setUp() {
        xml = new XMLResource("<?xml version='1.0' encoding='ISO-8859-1'?>" +
        	"<!-- Edited by XMLSpy® --><breakfast_menu> " +
        	"<info id='50'>Hello</info> " +
        	"<foo><details stale='true'><string id='1'>String Word</string><float>10.54</float></details></foo>" +
        	"    <food active='false'>        <name>Belgian Waffles</name>        " +
        	"<price foo='bar'>$5.95</price> <description>two of our famous Belgian Waffles with plenty " +
        	"of real maple syrup</description>        <calories>650</calories>    </food>    <food attr='10'>        " +
        	"<name>Strawberry Belgian Waffles</name>        <price t='a'>$7.95</price>        " +
        	"<description>light Belgian waffles covered with strawberries and whipped cream</description>" +
        	"<calories>900</calories>    </food>    <food>        <name>Berry-Berry Belgian Waffles</name> " +
        	"<price>$8.95</price>        <description>light Belgian waffles covered with an assortment of" +
        	" fresh berries and whipped cream</description>        <calories>900</calories>    </food>    " +
        	"<food>        <name>French Toast</name>        <price>$4.50</price>        <description>thick " +
        	"slices made from our homemade sourdough bread</description>        " +
        	"<calories>600</calories>    </food>    <food>        <name>Homestyle Breakfast</name>        " +
        	"<price>$6.95</price>        <description>two eggs, bacon or sausage, toast, and our ever-popular " +
        	"hash browns</description>        <calories>950</calories>    </food></breakfast_menu>");
    }
    
    // GET(STRING)
    
    @Test
    public void shouldGetTheRootAsResource() {
        Resource root = (Resource) xml.get("breakfast_menu");
        Assert.assertTrue(root instanceof XMLResource);
    }
    
    @Test
    public void shouldGetSecondLevelElementValue() {
        Resource root = (Resource) xml.get("breakfast_menu");
        String prop = (String) root.get("info");
        Assert.assertEquals("Hello", prop);
    }
    
    @Test
    public void shouldGetSecondLevelElementAttribute() {
        Resource root = (Resource) xml.get("breakfast_menu");
        String id = (String) root.get("info#id");
        Assert.assertEquals("50", id);
    }
    
    @Test
    public void shouldGetSecondLevelElementAsResource() {
        Resource root = (Resource) xml.get("breakfast_menu");
        Resource firstFood = (Resource) root.get("food");
        Assert.assertTrue(firstFood instanceof XMLResource);
    }
    
    @Test
    public void shouldGetSecondLevelElementChildValue() {
        Resource root = (Resource) xml.get("breakfast_menu");
        Resource firstFood = (Resource) root.get("food"); //2nd level
        String price = (String) firstFood.get("price");   //2nd level child
        Assert.assertEquals("$5.95", price);
    }
    
    @Test
    public void shouldGetSecondLevelElementChildAttribute() {
        Resource root = (Resource) xml.get("breakfast_menu");
        Resource firstFood = (Resource) root.get("food"); //2nd level
        String foo = (String) firstFood.get("price#foo");   //2nd level child attribute
        Assert.assertEquals("bar", foo);
    }
    
    @Test
    public void shouldGetThirdLevelElementChildValue() {
        Resource root = (Resource) xml.get("breakfast_menu");
        Resource foo = (Resource) root.get("foo");          //2nd level
        Resource details = (Resource) foo.get("details"); //3rd level
        String numeric = (String) details.get("float");   //3rd level child
        Assert.assertEquals(10.54, Float.valueOf(numeric), 2);
    }
    
    @Test
    public void shouldGetThirdLevelElementAttributeValue() {
        Resource root = (Resource) xml.get("breakfast_menu");
        String isStale = (String) root.get(1, "details#stale"); //3rd level attribute
        Assert.assertEquals(true, Boolean.valueOf(isStale).booleanValue());
    }
    
    @Test
    public void shouldGetSecondLevelElementAttributeByProperty() {
    	Resource food = xml.getAsResource(3); // 2nd food element
        Assert.assertEquals("10", food.get("#attr"));
    }
    
    // GET (INT)
    
    @Test
    public void shouldGetTheRootAsResourceByIndex() {
        Resource root = (Resource) xml.get(0);
        Assert.assertTrue(root instanceof XMLResource);
        Assert.assertTrue(root.get("food") instanceof XMLResource);
    }
    
    @Test
    public void shouldGetSecondLevelElementValue2() {
        Resource root = (Resource) xml.get(0);
        String prop = (String) root.get("info");
        Assert.assertEquals("Hello", prop);
    }
    
    @Test
    public void shouldGetSecondLevelElementAttribute2() {
        Resource root = (Resource) xml.get(0);
        String id = (String) root.get("info#id");
        Assert.assertEquals("50", id);
    }
    
    @Test
    public void shouldGetSecondLevelElementChildValue2() {
        Resource root = (Resource) xml.get(0);
        Resource firstFood = (Resource) root.get(2); //2nd level
        String price = (String) firstFood.get("price");   //2nd level child
        Assert.assertEquals("$5.95", price);
    }
    
    
    // GET(INT, STRING)
    
    @Test
    public void shouldGetSecondLevelElementValueByIndexAndProperty() {
        Assert.assertEquals("Hello", xml.get(0, "info"));
    }
    
    @Test
    public void shouldGetSecondLevelElementAttributeByIndexAndProperty() {
    	Assert.assertEquals("50", xml.get(0, "info#id"));
    }
    
    @Test
    public void shouldGetSecondLevelElementChildValueByIndexAndProperty() {
        Resource root = (Resource) xml.get("breakfast_menu");
        Assert.assertEquals("$5.95", root.get(2, "price"));
    }
    
    @Test
    public void shouldGetSecondLevelElementAttributeByIndexAndProperty2() {
        Assert.assertEquals("10", xml.get(3, "#attr"));
    }
    
    @Test
    public void shouldGetThirdLevelElementAttribute() {
        Resource foo = xml.getAsResource("foo");
        Resource details = foo.getAsResource("details");
        Assert.assertEquals("1", details.get(0, "#id"));
    }
    
    // SET(STRING)
    
    @Test
    public void shouldSetSecondLevelElementValue() {
    	Resource root = (Resource) xml.get(0);
    	String newInfoValue = "Hello World!";
    	root.set("info", newInfoValue);
    	Assert.assertEquals(newInfoValue, root.get("info"));
    }
    
    @Test
    public void shouldSetSecondLevelElementAttribute() {
    	Resource root = (Resource) xml.get(0);
    	Assert.assertEquals("50", root.get("info#id"));
    	String newId = "51";
    	root.set("info#id", newId);
    	Assert.assertEquals(newId, root.get("info#id"));
    }
    
    @Test
    public void shouldSetSecondLevelElementChildAttribute() {
        Resource root = (Resource) xml.get(0);
        Resource firstFood = (Resource) root.get(2); //2nd level food
        Assert.assertEquals("bar", firstFood.get("price#foo"));
        firstFood.set("price#foo", "baz");
        Assert.assertEquals("baz", xml.getAsResource(0).getAsResource(2).get("price#foo"));
    }
    
    @Test
    public void shouldSetThirdLevelElementValue() {
    	Resource root = (Resource) xml.get(0);
    	Assert.assertEquals(10.54f, 
    						Float.valueOf((String) root.getAsResource("foo").getAsResource(0).get("float")), 
    						2);
    	root.getAsResource("foo").getAsResource("details").set("float", 10.55);
    	Assert.assertEquals(10.55f, 
				Float.valueOf((String) root.getAsResource("foo").getAsResource(0).get("float")), 
				2);
    }
    
    // SET(INT, OBJECT)
    
    @Test
    public void shouldSetSecondLevelElementValue2() {
    	String newInfoValue = "Hello World!";
    	xml.set(0, "info", newInfoValue);
    	Assert.assertEquals(newInfoValue, xml.get(0, "info"));
    }
    
    @Test
    public void shouldSetThirdLevelElementValue2() {
    	Resource root = (Resource) xml.get(0);
    	Assert.assertEquals(10.54f, 
    						Float.valueOf((String) root.getAsResource("foo").getAsResource(0).get("float")), 
    						2);
    	root.getAsResource("foo").getAsResource("details").set(1, 10.55);
    	Assert.assertEquals(10.55f, 
				Float.valueOf((String) root.getAsResource("foo").getAsResource(0).get("float")), 
				2);
    }
    
    // SET(INT, STRING, OBJECT)
    
    @Test
    public void shouldSetThirdLevelElementValue3() {
    	Resource root = (Resource) xml.get(0);
    	Assert.assertEquals(10.54f, 
    						Float.valueOf((String) root.getAsResource("foo").getAsResource(0).get("float")), 
    						2);
    	root.getAsResource("foo").set(0, "float", 10.55);
    	Assert.assertEquals(10.55f, 
				Float.valueOf((String) root.getAsResource("foo").getAsResource(0).get("float")), 
				2);
    }
    
    @Test
    public void shouldSetSecondFoodCaloriesWithNewValue() {
    	Assert.assertEquals("900", xml.getAsResource(3).get("calories"));
    	xml.set(3, "calories", 1200);
    	Assert.assertEquals("1200", xml.getAsResource(3).get("calories"));
    }
    
    @Test
    public void shouldSetSecondLevelElementAttribute2() {
    	Resource root = (Resource) xml.get(0);
    	Assert.assertEquals("50", root.get("info#id"));
    	String newId = "51";
    	xml.set(0, "info#id", newId);
    	Assert.assertEquals(newId, root.get("info#id"));
    }
    
    @Test
    public void shouldSetSecondLevelElementChildAttribute2() {
        Resource root = (Resource) xml.get(0);
        Resource firstFood = (Resource) root.get(2); //2nd level food
        Assert.assertEquals("bar", firstFood.get("price#foo"));
        xml.set(2, "price#foo", "baz");
        Assert.assertEquals("baz", xml.getAsResource(0).getAsResource(2).get("price#foo"));
    }
}
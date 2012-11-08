package br.com.rpk.restc.impl;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import br.com.rpk.restc.Resource;

public class XMLResource implements Resource {
    
    private Document xmlDoc;
    private Node currentNode;
    private NodeList children;
    
    XMLResource(String xml) {
        this.xmlDoc = parse(xml);
    }
    
    private XMLResource(Node currentNode, Document xmlDoc) {
    	this.currentNode = currentNode;
        this.children = getChildrenOf(currentNode);
        this.xmlDoc = xmlDoc;
    }

	private Document parse(String xml) {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = fac.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse server XML response", e);
        } 
    }

    @Override
    public Object get(String property) {
        if (isFindingForAttribute(property)) {
        	if (isFindingInCurrentNode(property)) {
        		return findAttributeForCurrentNode(property);
        	} else {
        		return findAttributeForNode(property);
        	}
        } else {
            Node node = getNode(property);
            if (hasChildren(node)) {
                return new XMLResource(node, xmlDoc);
            } else {
                return node.getTextContent();
            }
        }
    }

    private Object findAttributeForCurrentNode(String property) {
        String attribute = property.split("#")[1];
        NamedNodeMap attrs = currentNode.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            if (attr.getNodeName().equals(attribute)) {
                return attr.getNodeValue();
            }
        }
        throw new RuntimeException("Attribute not found: " + attribute);
	}

	private String findAttributeForNode(String property) {
        Node node = getNode(property.split("#")[0]);
        String attribute = property.split("#")[1];
        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            if (attr.getNodeName().equals(attribute)) {
                return attr.getNodeValue();
            }
        }
        throw new RuntimeException("Attribute not found: " + attribute);
    }

    private boolean hasChildren(Node node) {
        NodeList possibleChildren = node.getChildNodes();
        for (int i = 0; i < possibleChildren.getLength(); i++) {
            Node child = possibleChildren.item(i);
            if (child.getNodeType() != Node.TEXT_NODE &&
                child.getNodeType() != Node.COMMENT_NODE)
                return true;
        }
        return false;
    }
    
    private NodeList getChildrenOf(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE ||
                child.getNodeType() == Node.COMMENT_NODE)
                child.getParentNode().removeChild(child);
        }
        return children;
    }

    private Node getNode(String property) {
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (property.equals(node.getNodeName())) {
                    return node;
                }
            }
        } else {
            return xmlDoc.getElementsByTagName(property).item(0);
        }
        throw new RuntimeException("Element not found: " + property);
    }

    private boolean isFindingForAttribute(String property) {
        return property.contains("#");
    }

    @Override
    public Object get(int index, String property) {
   		return getAsResource(index).get(property);
    }

    private boolean isFindingInCurrentNode(String property) {
		return property.split("#")[0].isEmpty();
	}

	@Override
    public Object get(int index) {
        Node node = getNodeAt(index);
//        if (hasChildren(node)) {
            return new XMLResource(node, xmlDoc);
//        } else {
//            return node.getTextContent();
//        }
    }

    private Node getNodeAt(int index) {
        if (children != null) {
            if (children.getLength() >= index + 1) {
                return children.item(index);
            }
        } else {
            Node root = getDocumentRoot();
            if (index == 0) {
                return root;
            } else {
                return getChildrenOf(root).item(index);
            }
        }
        throw new RuntimeException("Element not found at index: " + index);
    }

    private Node getDocumentRoot() {
        NodeList children = xmlDoc.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() != Node.TEXT_NODE &&
                child.getNodeType() != Node.COMMENT_NODE)
                return child;
        }
        throw new RuntimeException("Root not found!");
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
    public void set(String property, Object value) {
    	if (isSettingAttribute(property)) {
    		setAttributeAtNode(property, value);
    	} else {
    		getNode(property).setTextContent(value.toString());
    	}
    }

    private void setAttributeAtNode(String property, Object value) {
		Node node = getNode(property.split("#")[0]);
		String attribute = property.split("#")[1];
		NamedNodeMap attrs = node.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            if (attr.getNodeName().equals(attribute)) {
                attr.setTextContent(value.toString());
                return;
            }
        }
        throw new RuntimeException("Attribute not found: " + attribute);
	}

	private boolean isSettingAttribute(String property) {
    	return isFindingForAttribute(property);
	}

	@Override
    public void set(int index, String property, Object value) {
		if (isSettingAttribute(property)) {
    		setAttributeAtNode(property, value);
    		return;
    	}
        Node node = getNodeAt(index);
        NodeList children = getChildrenOf(node);
        for (int i = 0; i < children.getLength(); i++) {
        	Node child = children.item(i);
    		if (property.equals(child.getNodeName())) {
        		child.setTextContent(value.toString());
        		return;
        	}
        }
        throw new RuntimeException("Attribute not updated at child " + index + 
        		", element " + property + " with value " + value);
    }

    @Override
    public void set(int index, Object value) {
    	getNodeAt(index).setTextContent(value.toString());
    }

    @Override
    public String toJSON() {
    	return "";
    }

    @Override
    public String toXML() {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "NO");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot execute toXML", e);
        }
    }
}
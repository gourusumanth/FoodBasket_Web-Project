import java.io.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SaxParserRestaurants extends DefaultHandler {

    private Restaurant restaurant;
    private static Map<String,Restaurant> restaurantMap = new HashMap<String,Restaurant>();
    private String elementValueRead;

    public SaxParserRestaurants(String restaurantXML) {
        ParseDocument(restaurantXML);
    }

    private void ParseDocument(String restaurantXML) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(restaurantXML, this);
            MySQLDataStoreUtilities.clearRestaurants();
            MySQLDataStoreUtilities.saveRestaurants(restaurantMap);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {
        if (elementName.equalsIgnoreCase("restaurant")) {
            restaurant = new Restaurant();
            restaurant.setId(attributes.getValue("id"));
            restaurantMap.put(restaurant.getId(),restaurant);
        }
    }

    @Override
    public void endElement(String str1, String str2, String element) throws SAXException {
        if (element.equalsIgnoreCase("name")) {
            restaurant.setName(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("tagline")) {
            restaurant.setTagLine(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("time")) {
            restaurant.setTime(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("expensiveMeter")) {
            restaurant.setExpensiveMeter(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("minimumAmount")) {
            restaurant.setMinimumAmount(Float.parseFloat(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("image")) {
            restaurant.setImage(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("deliveryFee")) {
            restaurant.setDeliverFee(Float.parseFloat(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("zipcode")) {
            restaurant.setZipCode(elementValueRead);
            return;
        }

    }

    @Override
    public void characters(char[] content, int begin, int end) throws SAXException {
        elementValueRead = new String(content, begin, end);
    }

    public static void parseXML(String restaurantXML){
        new SaxParserRestaurants(restaurantXML);
    }

}

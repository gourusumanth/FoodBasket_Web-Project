import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SaxParserMenuItems extends DefaultHandler {

    private MenuItems restaurantMenuItems;
    private String elementValueRead;
    private List<MenuItems> menuItemsList = new ArrayList<>();

    public SaxParserMenuItems(String menuItemsXML) {
        ParseDocument(menuItemsXML);
    }

    private void ParseDocument(String menuItemsXML) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(menuItemsXML, this);
            MySQLDataStoreUtilities.clearMenuItems();
            MySQLDataStoreUtilities.saveMenuItems(menuItemsList);
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
        if (elementName.equalsIgnoreCase("menuItem")) {
            restaurantMenuItems = new MenuItems();
            restaurantMenuItems.setId(attributes.getValue("id"));
            restaurantMenuItems.setRestaurantId(attributes.getValue("restaurantId"));
            menuItemsList.add(restaurantMenuItems);
        }
        if(elementName.equalsIgnoreCase("similarMenuItems")){
            ArrayList<String> items = new ArrayList<String>(Arrays.asList(attributes.getValue("id").split(" ")));
            restaurantMenuItems.setSimilarMenuItems(items);
        }
    }


    @Override
    public void endElement(String str1, String str2, String element) throws SAXException {
        if (element.equalsIgnoreCase("name")) {
            restaurantMenuItems.setName(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("ingredients")) {
            restaurantMenuItems.setIngredients(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("price")) {
            restaurantMenuItems.setPrice(Float.parseFloat(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("image")) {
            restaurantMenuItems.setImage(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("sale")) {
            if (elementValueRead.equals("1")) {
              restaurantMenuItems.setSale(true);
              return;
            } else {
              restaurantMenuItems.setSale(false);
              return;
            }
        }
        if(element.equalsIgnoreCase("quantity")) {
          restaurantMenuItems.setAvailability(Integer.parseInt(elementValueRead));
          return;
        }
    }

    @Override
    public void characters(char[] content, int begin, int end) throws SAXException {
        elementValueRead = new String(content, begin, end);
    }

    public static void parseXML(String menuItemsXML){
        new SaxParserMenuItems(menuItemsXML);
    }


}

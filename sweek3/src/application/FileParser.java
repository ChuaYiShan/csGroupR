package application;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FileParser {

	ArrayList<CircuitElement> newList = new ArrayList<CircuitElement>();
	String tmpValue;
	CircuitElement tmpElement;

	public void addToList (CircuitElement aTest){
		newList.add(aTest);
	}

	public ArrayList<CircuitElement> getList (){
		return this.newList;
	}

	public ArrayList<CircuitElement> parse (String fileName){
		
		newList = new ArrayList<CircuitElement>();

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean b2 = false;
				boolean b3 = false;
				boolean b4 = false;

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

				//	System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("nodes")) {
						tmpElement = new CircuitElement();
						tmpElement.setId(attributes.getValue("ID"));
						newList.add(tmpElement);
					}

					if (qName.equalsIgnoreCase("XCOORD")) {
						b2 = true;
					}

					if (qName.equalsIgnoreCase("YCOORD")) {
						b3 = true;
					}

					if (qName.equalsIgnoreCase("TYPE")) {
						b4 = true;
					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
		//			System.out.println("End Element :" + qName);
				}

				public void characters(char ch[], int start, int length) throws SAXException {

					if (b2) {
						//System.out.println("X: " + new String(ch, start, length));
						tmpElement.setxCoord(new String(ch,start,length));
						b2 = false;
					}

					if (b3) {
						//	System.out.println("Y: " + new String(ch, start, length));
						tmpElement.setyCoord(new String(ch,start,length));
						b3 = false;
					}

					if (b4) {
						//		System.out.println("Type: " + new String(ch, start, length));
						tmpElement.setType(new String(ch,start,length));
						b4 = false;
					}

					//	System.out.println("char");

				}
			};
			
			saxParser.parse(fileName, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newList;
	}
}

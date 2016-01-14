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

				boolean xcoord = false;
				boolean ycoord = false;
				boolean source = false;
				boolean target = false;
				boolean type = false;

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if (qName.equalsIgnoreCase("node") || qName.equalsIgnoreCase("wire")) {
						tmpElement = new CircuitElement();
						tmpElement.setId(attributes.getValue("ID"));
						newList.add(tmpElement);
					}

					if (qName.equalsIgnoreCase("XCOORD")) {
						xcoord = true;
					}

					if (qName.equalsIgnoreCase("YCOORD")) {
						ycoord = true;
					}

					if (qName.equalsIgnoreCase("source")) {
						source = true;
					}

					if (qName.equalsIgnoreCase("target")) {
						target = true;
					}

					if (qName.equalsIgnoreCase("TYPE")) {
						type = true;
					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					// System.out.println("End Element :" + qName);
				}

				public void characters(char ch[], int start, int length) throws SAXException {

					if (xcoord) {
						//System.out.println("X: " + new String(ch, start, length));
						tmpElement.setxCoord(new String(ch,start,length));
						xcoord = false;
					}

					if (ycoord) {
						//	System.out.println("Y: " + new String(ch, start, length));
						tmpElement.setyCoord(new String(ch,start,length));
						ycoord = false;
					}

					if (source) {
						//	System.out.println("Y: " + new String(ch, start, length));
						tmpElement.setSource(new String(ch,start,length));
						source = false;
					}

					if (target) {
						//	System.out.println("Y: " + new String(ch, start, length));
						tmpElement.setTarget(new String(ch,start,length));
						target = false;
					}

					if (type) {
						//		System.out.println("Type: " + new String(ch, start, length));
						tmpElement.setType(new String(ch,start,length));
						type = false;
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

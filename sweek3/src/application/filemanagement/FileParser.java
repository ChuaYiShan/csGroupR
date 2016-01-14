package application.filemanagement;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FileParser {

	ArrayList<CircuitElement> newList = new ArrayList<CircuitElement>();
	CircuitElement tmpElement;

	public void addToList (CircuitElement element){
		newList.add(element);
	}

	public ArrayList<CircuitElement> getList (){
		return this.newList;
	}

	public ArrayList<CircuitElement> parse(String fileName){

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
						tmpElement.setxCoord(new String(ch,start,length));
						xcoord = false;
					}

					if (ycoord) {
						tmpElement.setyCoord(new String(ch,start,length));
						ycoord = false;
					}

					if (source) {
						tmpElement.setSource(new String(ch,start,length));
						source = false;
					}

					if (target) {
						tmpElement.setTarget(new String(ch,start,length));
						target = false;
					}

					if (type) {;
					tmpElement.setType(new String(ch,start,length));
					type = false;
					}

				}
			};

			saxParser.parse(fileName, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newList;
	}
}
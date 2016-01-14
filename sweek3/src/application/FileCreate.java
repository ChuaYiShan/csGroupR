package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;

class FileCreate implements Serializable {

	public Object stringToObj(String s) throws IOException, ClassNotFoundException {
		byte [] data = Base64.getDecoder().decode( s );
		ObjectInputStream ois = new ObjectInputStream( 
				new ByteArrayInputStream(  data ) );
		Object o  = ois.readObject();
		ois.close();
		return o;
	}

	private static String objToString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject(o);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}

	public String savefile(AnchorPane right_pane) {
		
		String xmlString=null;
		try {

			StringWriter stringWriter = new StringWriter();

			XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();	
			XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

			xMLStreamWriter.writeStartDocument();

			xMLStreamWriter.writeStartElement("DummyItem");

			for (Node aNode: right_pane.getChildren())
			{
				if (!(aNode.getLayoutX() > 0.0)) {
					continue;
				}
				
				if (aNode instanceof TextFlow) {
					continue;
				}
				
				Component c = (Component) aNode;
				
				xMLStreamWriter.writeStartElement("Nodes");
				xMLStreamWriter.writeAttribute("ID", c.getId());

				xMLStreamWriter.writeStartElement("xCoord");
				xMLStreamWriter.writeCharacters(String.valueOf(c.getLayoutX()));
				xMLStreamWriter.writeEndElement();

				xMLStreamWriter.writeStartElement("yCoord");
				xMLStreamWriter.writeCharacters(String.valueOf(c.getLayoutY()));
				xMLStreamWriter.writeEndElement();

				xMLStreamWriter.writeStartElement("type");
				xMLStreamWriter.writeCharacters(objToString(c.getType()));
				xMLStreamWriter.writeEndElement();

				xMLStreamWriter.writeEndElement();
			}

			xMLStreamWriter.writeEndElement();

			//////////
			xMLStreamWriter.writeEndDocument();

			xMLStreamWriter.flush();
			xMLStreamWriter.close();

			xmlString = stringWriter.getBuffer().toString();

			stringWriter.close();

			// System.out.println(xmlString);

		} catch (XMLStreamException e) {
			e.printStackTrace();
			System.out.println("1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("2");
		}
		return xmlString;
	}
}

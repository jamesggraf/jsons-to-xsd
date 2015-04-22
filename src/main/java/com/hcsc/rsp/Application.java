package com.hcsc.rsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.ethlo.schematools.jsons2xsd.Jsons2Xsd;
import com.ethlo.schematools.jsons2xsd.Jsons2Xsd.OuterWrapping;

public class Application {
	public static void main(String[] args) {
		new Application().run(args[0]);
	}

	public void run(String jsonFileName) {
		try {
			final Document xsdDocument = convertJsonSchemaToXsd(jsonFileName);
			printDocument(xsdDocument, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Reader loadJson(File jsonFile) throws FileNotFoundException {
		return new BufferedReader(new FileReader(jsonFile));
	}
	
	private Document convertJsonSchemaToXsd(String jsonFileName) throws IOException {
		final File file = new File(jsonFileName);
		final Reader jsonSchema = loadJson(file);
		final String name = file.getName().replace(".json", "");
		final String targetNameSpaceUri = "http://rsp.hcsc.com/ns";
		final OuterWrapping wrapping = OuterWrapping.TYPE;
		
		return Jsons2Xsd.convert(jsonSchema, targetNameSpaceUri, wrapping, name);
	}
	
	private void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(doc), 
	         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
}

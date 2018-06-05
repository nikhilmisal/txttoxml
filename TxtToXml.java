import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class TxtToXml {

    BufferedReader in;
    StreamResult out;
    TransformerHandler th;
    AttributesImpl attr;

    public static void main(String args[]) {
    	TxtToXml tx = new TxtToXml();
        tx.process();
    }

    public void process() {
    	try{
            in = new BufferedReader(new FileReader("dummy.txt"));
            out = new StreamResult("dummy.xml");
            initializeXML();
            th.startElement("", "", "Library", attr);
            String line;
            while ((line= in.readLine()) != null) {
            	parse(line);
            }
            th.endElement("", "", "Library");
            th.endDocument();
            in.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public void initializeXML() throws ParserConfigurationException,TransformerConfigurationException, SAXException 
    {
        SAXTransformerFactory tf = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

        th = tf.newTransformerHandler();
        Transformer serializer = th.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        th.setResult(out);
        th.startDocument();
        attr = new AttributesImpl();
    }

    public void parse(String s) throws SAXException {
        String[] parts = s.split(";");
        attr.clear();
        th.startElement("", "", "Book", attr);
        th.startElement("", "", "ISBN", attr);
        	th.characters(parts[0].toCharArray(), 0, parts[0].length());
        th.endElement("", "", "ISBN");
        th.startElement("", "", "Authors", attr);
        String[] authors = parts[1].split(",");
        for(int i=0;i<authors.length;i++){
        	th.startElement("","", "AuthorName",attr);
            th.characters(authors[i].toCharArray(), 0, authors[i].length());
            th.endElement("","", "AuthorName");
        }
        th.endElement("", "","Authors");
        th.startElement("","", "Title", attr);
        	th.characters(parts[2].toCharArray(), 0, parts[2].length());
        th.endElement("", "","Title");
        th.endElement("", "", "Book");
    }
}

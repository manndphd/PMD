package vn.dongpv.pde.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class provides some utilities to handle the XML document.
 * 
 * @author Pham Van Dong
 * 
 * @see DocumentBuilder
 * @see XPath
 * 
 */
public class XMLUtil
{

	private XMLUtil()
	{
	}

	/**
	 * Creates new {@link DocumentBuilder}.
	 * 
	 * @return new {@link DocumentBuilder}.
	 * @throws AnalyzerConfigurationException
	 */
	public static DocumentBuilder newBuilder()
		throws ParserConfigurationException
	{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	/**
	 * Creates new {@link XPath}.
	 * 
	 * @return new {@link XPath}.
	 */
	public static XPath newXPath()
	{
		return XPathFactory.newInstance().newXPath();
	}

	/**
	 * Analyzes the XML file that located by the filePath and returns the
	 * {@link Document} that analyzed.
	 * 
	 * @param filePath
	 *            the specified file path of the XML document.
	 * @return the {@link Document}.
	 * @throws SAXException
	 * @throws IOException
	 * @throws AnalyzerConfigurationException
	 * @throws URISyntaxException
	 */
	public static Document analyze(String filePath)
		throws SAXException, IOException, ParserConfigurationException, URISyntaxException
	{
		ValidatorUtil.checkNotNull(filePath);

		final InputStream stream = ResourceUtil.getInputStream(filePath);
		if (stream == null)
		{
			return null;
		}

		final Document doc = newBuilder().parse(stream);
		stream.close();
		return doc;
	}

	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException
	{
		throw new java.lang.CloneNotSupportedException();
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

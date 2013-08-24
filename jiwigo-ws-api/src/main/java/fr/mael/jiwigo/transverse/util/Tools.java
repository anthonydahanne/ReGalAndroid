package fr.mael.jiwigo.transverse.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.mael.jiwigo.service.impl.CategoryServiceImpl;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;

/*
 *  jiwigo-ws-api Piwigo webservice access Api
 *  Copyright (c) 2010-2011 Mael mael@le-guevel.com
 *                All Rights Reserved
 *
 *  This library is free software. It comes without any warranty, to
 *  the extent permitted by applicable law. You can redistribute it
 *  and/or modify it under the terms of the Do What The Fuck You Want
 *  To Public License, Version 2, as published by Sam Hocevar. See
 *  http://sam.zoy.org/wtfpl/COPYING for more details.
 */
/**

 * @author mael
 *
 */
public class Tools {
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    //    /**
    //     * Transformation of an inpustream into string.<br/>
    //     * useful to read the result of the webservice
    //     * @param input the stream
    //     * @return the string
    //     * @throws IOException
    //     */
    //    public static String readInputStreamAsString(InputStream input) throws IOException {
    //	StringWriter writer = new StringWriter();
    //	InputStreamReader streamReader = new InputStreamReader(input);
    //	BufferedReader buffer = new BufferedReader(streamReader);
    //	String line = "";
    //	while (null != (line = buffer.readLine())) {
    //	    writer.write(line);
    //	}
    //	return writer.toString();
    //    }

    /**
     * Transformation of an inpustream into string.<br/>
     * useful to read the result of the webservice
     * @param input the stream
     * @return the string
     * @throws IOException
     */
    public static String readInputStreamAsString(InputStream is) throws IOException {
	if (is != null) {
	    Writer writer = new StringWriter();

	    char[] buffer = new char[1024];
	    try {
		Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		int n;
		while ((n = reader.read(buffer)) != -1) {
		    writer.write(buffer, 0, n);
		}
	    } finally {
		is.close();
	    }
	    return writer.toString();
	} else {
	    return "";
	}
    }

    /**
     * String to document
     * @param string the string
     * @return the corresponding document
     * @throws JDOMException
     * @throws IOException
     */
    public static Document stringToDocument(String xmlSource) throws SAXException, ParserConfigurationException,
	    IOException {
	LOG.debug(xmlSource);
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	return builder.parse(new InputSource(new StringReader(xmlSource)));
    }

    /**
     * Inputstream to document
     * @param input the inputStream
     * @return the document
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws JDOMException
     * @throws IOException
     */
    public static Document readInputStreamAsDocument(InputStream input) throws SAXException,
	    ParserConfigurationException, IOException {
	return stringToDocument(readInputStreamAsString(input));
    }

    /**
     * Document to string
     * @param doc the document to transform
     * @return the string
     */
    public static String documentToString(Node node) {
	try {
	    Source source = new DOMSource(node);
	    StringWriter stringWriter = new StringWriter();
	    Result result = new StreamResult(stringWriter);
	    TransformerFactory factory = TransformerFactory.newInstance();
	    Transformer transformer = factory.newTransformer();
	    transformer.transform(source, result);
	    return stringWriter.getBuffer().toString();
	} catch (TransformerConfigurationException e) {
	    e.printStackTrace();
	} catch (TransformerException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Function that gets the url of a file
     * Useful to get the images that are in the jar
     * @param fileName the path of the file
     * @return the url of the file
     */
    public static URL getURL(String fileName) {
	URLClassLoader urlLoader = (URLClassLoader) Tools.class.getClassLoader();
	URL fileLocation = urlLoader.findResource(fileName);
	return fileLocation;
    }

    /**
     * gets the md5 sum of a file
     * @param filename the path of the file
     * @return the checksum
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public static String getMD5Checksum(String filename) throws NoSuchAlgorithmException, IOException {
	byte[] b = createChecksum(filename);
	String result = "";
	for (int i = 0; i < b.length; i++) {
	    result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
	}
	return result;
    }

    /**
     * Creation of the checksum of a file
     * @param filename the path of the file
     * @return the checksum as array byte
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws Exception
     */
    private static byte[] createChecksum(String filename) throws NoSuchAlgorithmException, IOException {
	InputStream fis = new FileInputStream(filename);

	byte[] buffer = new byte[1024];
	MessageDigest complete = MessageDigest.getInstance("MD5");
	int numRead;
	do {
	    numRead = fis.read(buffer);
	    if (numRead > 0) {
		complete.update(buffer, 0, numRead);
	    }
	} while (numRead != -1);
	fis.close();
	return complete.digest();
    }

    /**
     * File to array bytes
     * @param file the file
     * @return the array bytes
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
	InputStream is = new FileInputStream(file);
	long length = file.length();

	byte[] bytes = new byte[(int) length];
	int offset = 0;
	int numRead = 0;
	while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
	    offset += numRead;
	}

	if (offset < bytes.length) {
	    throw new IOException("Could not completely read file " + file.getName());
	}
	is.close();
	return bytes;
    }

    /**
     * Function that checks if the webservice response is ok
     * @param doc the document from the webservice
     * @return true if ok
     */
    public static boolean checkOk(Document doc) throws FileAlreadyExistsException {
	if (doc.getDocumentElement().getAttribute("stat").equals("ok")) {
	    return true;
	} else {
	    LOG.error("Resultat : " + doc.getDocumentElement().getAttribute("stat") + "\nDocument retourne : \n"
		    + Tools.documentToString(doc));
	    if (((Element) doc.getDocumentElement().getElementsByTagName("err").item(0)).getAttribute("msg").equals(
		    "file already exists")) {
		throw new FileAlreadyExistsException("The file already exists on the server");
	    }
	    return false;
	}

    }

    /**
     * Exception to string
     * @param aThrowable exception
     * @return l'exception en string
     */
    public static String getStackTrace(Throwable aThrowable) {
	final Writer result = new StringWriter();
	final PrintWriter printWriter = new PrintWriter(result);
	aThrowable.printStackTrace(printWriter);
	return result.toString();
    }

    /**
     * Function that splits a file
     * @param fichier the file to split
     * @param size the size of the resulting chunks
     * @return the list of files
     * @throws IOException
     */
    //feature:0001827
    public static ArrayList<File> splitFile(File fichier, int size) throws IOException {
	FileInputStream fis = new FileInputStream(fichier);
	byte buffer[] = new byte[size];
	ArrayList<File> listFichiers = new ArrayList<File>();
	int count = 0;
	while (true) {
	    int i = fis.read(buffer, 0, size);
	    if (i == -1)
		break;
	    File file = new File(System.getProperty("java.io.tmpdir") + "/tempcut" + count);
	    listFichiers.add(file);
	    FileOutputStream fos = new FileOutputStream(file);
	    fos.write(buffer, 0, i);
	    fos.flush();
	    fos.close();

	    ++count;
	}
	return listFichiers;
    }

    /**
     * Function used to put the exif and iptc metadata from one image to another
     * @param enriched original image where the metadata comes from
     * @param naked image where to put metadata
     * @return enriched image
     * @throws Exception
     */
    public static byte[] enrich(byte[] enriched, byte[] naked) throws Exception {

	//	// read IPTC metadata from the original enriched image
	//	IImageMetadata metadata = Sanselan.getMetadata(enriched);
	//	JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	//	JpegPhotoshopMetadata photoshopMetadata = jpegMetadata.getPhotoshop();
	//	if (photoshopMetadata == null) {
	//	    return naked;
	//	}
	//
	//	PhotoshopApp13Data data = photoshopMetadata.photoshopApp13Data;
	//
	//	// read the EXIF metadata from the parsed JPEG metadata
	//	TiffOutputSet outputSet = jpegMetadata.getExif().getOutputSet();
	//
	//	// enrich the naked byte[] with EXIF metadata
	//	ExifRewriter writer = new ExifRewriter();
	//	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	//	writer.updateExifMetadataLossless(naked, outputStream, outputSet);
	//
	//	// enrich the partially clothed byte[] with IPTC metadata
	//	InputStream src = new ByteArrayInputStream(outputStream.toByteArray());
	//	ByteArrayOutputStream dest = new ByteArrayOutputStream();
	//	new JpegIptcRewriter().writeIPTC(src, dest, data);
	//
	//	// return the fully clothed image as a byte[]
	//	return dest.toByteArray();
	return enriched;
    }

    /**
     * Bytes to file
     * @param fichier the file path
     * @param bytes the array bytes
     * @throws IOException
     */
    public static void byteToFile(String fichier, byte[] bytes) throws IOException {
	FileOutputStream fos = new FileOutputStream(fichier);
	fos.write(bytes);
	fos.flush();
	fos.close();

    }

    public static Document readFileAsDocument(String filePath) {
	Document doc = null;
	try {
	    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	    doc = docBuilder.parse(filePath);
	} catch (Exception e) {
	    LOG.error("Error converting file to Document : " + getStackTrace(e));
	}
	return doc;
    }

    /**
     * Gets the value of a document node
     * @param element
     * @param tagName
     * @return
     */
    public static String getStringValueDom(Element element, String tagName) {
	Element el = (Element) element.getElementsByTagName(tagName).item(0);
	if (el != null) {
	    return el.getFirstChild().getNodeValue();
	}
	// if not, empty string is safer then null
	return "";
    }

    /**
     * Sets the value of a document node
     * @param element
     * @param tagName
     * @param value
     */
    public static void setStringValueDom(Element element, String tagName, String value) {
	Element el = (Element) element.getElementsByTagName(tagName).item(0);
	el.setTextContent(value);
    }

    /**
     * Write an xml document to a file
     * @param doc document to write
     * @param filename file where to write the document
     */
    public static void writeXmlFile(Document doc, String filename) {
	try {
	    Source source = new DOMSource(doc);
	    File file = new File(filename);
	    Result result = new StreamResult(file);
	    Transformer xformer = TransformerFactory.newInstance().newTransformer();
	    xformer.transform(source, result);
	} catch (TransformerConfigurationException e) {
	    LOG.error(getStackTrace(e));
	} catch (TransformerException e) {
	    LOG.error(getStackTrace(e));
	}
    }
    //
    //    /**
    //     * Function used to get the md5 sum of a file
    //     * @param fileToTest the file 
    //     * @return the md5 sum
    //     * @throws NoSuchAlgorithmException
    //     * @throws FileNotFoundException
    //     * @throws JiwigoException 
    //     */
    //    public static String getMD5Sum(File file) throws NoSuchAlgorithmException, JiwigoException, FileNotFoundException {
    //	MessageDigest digest = MessageDigest.getInstance("MD5");
    //	InputStream is = new FileInputStream(file);
    //	byte[] buffer = new byte[8192];
    //	int read = 0;
    //	try {
    //	    while ((read = is.read(buffer)) > 0) {
    //		digest.update(buffer, 0, read);
    //	    }
    //	    byte[] md5sum = digest.digest();
    //	    BigInteger bigInt = new BigInteger(1, md5sum);
    //	    String output = bigInt.toString(16);
    //	    return output;
    //	} catch (IOException e) {
    //	    throw new JiwigoException("Unable to process file for MD5", e);
    //	} finally {
    //	    try {
    //		is.close();
    //	    } catch (IOException e) {
    //		throw new JiwigoException("Unable to close input stream for MD5 calculation", e);
    //	    }
    //	}
    //    }

}
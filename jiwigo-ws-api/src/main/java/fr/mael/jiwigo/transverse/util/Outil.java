package fr.mael.jiwigo.transverse.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.jpeg.iptc.JpegIptcRewriter;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
   Copyright (c) 2010, Mael
   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of jiwigo nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   DISCLAIMED. IN NO EVENT SHALL Mael BE LIABLE FOR ANY
   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
   
 
 * @author mael
 *
 */
public class Outil {
    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(Outil.class);

    /**
     * Transformation of an inpustream into string.<br/>
     * useful to read the result of the webservice
     * @param input the stream
     * @return the string
     * @throws IOException
     */
    public static String readInputStreamAsString(InputStream input) throws IOException {
	StringWriter writer = new StringWriter();
	InputStreamReader streamReader = new InputStreamReader(input);
	BufferedReader buffer = new BufferedReader(streamReader);
	String line = "";
	while (null != (line = buffer.readLine())) {
	    writer.write(line);
	}
	return writer.toString();
    }

    /**
     * String to document
     * @param string the string
     * @return the corresponding document
     * @throws JDOMException
     * @throws IOException
     */
    public static Document stringToDocument(String string) throws JDOMException, IOException {
	SAXBuilder sb = new SAXBuilder();
	Document doc = sb.build(new StringReader(string));
	return doc;

    }

    /**
     * Inputstream to document
     * @param input the inputStream
     * @return the document
     * @throws JDOMException
     * @throws IOException
     */
    public static Document readInputStreamAsDocument(InputStream input) throws JDOMException, IOException {
	return stringToDocument(readInputStreamAsString(input));
    }

    /**
     * Document to string
     * @param doc the document to transform
     * @return the string
     */
    public static String documentToString(Document doc) {
	return new XMLOutputter().outputString(doc);

    }

    /**
     * Function that gets the url of a file
     * Useful to get the images that are in the jar
     * @param fileName the path of the file
     * @return the url of the file
     */
    public static URL getURL(String fileName) {
	URLClassLoader urlLoader = (URLClassLoader) Outil.class.getClassLoader();
	URL fileLocation = urlLoader.findResource(fileName);
	return fileLocation;
    }

    /**
     * gets the md5 sum of a file
     * @param filename the path of the file
     * @return the checksum
     * @throws Exception
     */
    public static String getMD5Checksum(String filename) throws Exception {
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
     * @throws Exception
     */
    private static byte[] createChecksum(String filename) throws Exception {
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
    public static boolean checkOk(Document doc) {
	if (doc.getRootElement().getAttributeValue("stat").equals("ok")) {
	    return true;
	} else {
	    LOG.error("Resultat : " + doc.getRootElement().getAttributeValue("stat") + "\nDocument retourné : \n"
		    + Outil.documentToString(doc));
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

	// read IPTC metadata from the original enriched image
	IImageMetadata metadata = Sanselan.getMetadata(enriched);
	JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	JpegPhotoshopMetadata photoshopMetadata = jpegMetadata.getPhotoshop();
	if (photoshopMetadata == null) {
	    return naked;
	}

	PhotoshopApp13Data data = photoshopMetadata.photoshopApp13Data;

	// read the EXIF metadata from the parsed JPEG metadata
	TiffOutputSet outputSet = jpegMetadata.getExif().getOutputSet();

	// enrich the naked byte[] with EXIF metadata
	ExifRewriter writer = new ExifRewriter();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	writer.updateExifMetadataLossless(naked, outputStream, outputSet);

	// enrich the partially clothed byte[] with IPTC metadata
	InputStream src = new ByteArrayInputStream(outputStream.toByteArray());
	ByteArrayOutputStream dest = new ByteArrayOutputStream();
	new JpegIptcRewriter().writeIPTC(src, dest, data);

	// return the fully clothed image as a byte[]
	return dest.toByteArray();
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
}

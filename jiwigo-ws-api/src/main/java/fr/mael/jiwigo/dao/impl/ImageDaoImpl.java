package fr.mael.jiwigo.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.misc.BASE64Encoder;
import fr.mael.jiwigo.dao.ImageDao;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.exception.WrongChunkSizeException;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.session.impl.SessionManagerImpl;
import fr.mael.jiwigo.transverse.util.Tools;

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

 * Dao of the images
 * @author mael
 *
 */
public class ImageDaoImpl implements ImageDao {

    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(ImageDaoImpl.class);

    /**
     * cache to avoid downloading image for each access
     */
    private HashMap<Integer, List<Image>> cache;

    /**
     *
     */
    private Integer firstCatInCache;

    private SessionManager sessionManager;

    private ArrayList<File> filesToSend;

    public ImageDaoImpl() {
	cache = new HashMap<Integer, List<Image>>();
    }

    /**
     * Lists all images
     * @return the list of images
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> list(boolean refresh) throws JiwigoException {
	return listByCategory(null, refresh);
    }

    /**
     * Listing of the images for a category
     * @param categoryId the id of the category
     * @return the list of images
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> listByCategory(Integer categoryId, boolean refresh) throws JiwigoException {
	if (refresh || cache.get(categoryId) == null) {
	    Document doc = null;
	    if (categoryId != null) {
		doc = sessionManager.executeReturnDocument(MethodsEnum.LISTER_IMAGES.getLabel(), "cat_id",
			String.valueOf(categoryId));
	    } else {
		doc = sessionManager.executeReturnDocument(MethodsEnum.LISTER_IMAGES.getLabel());
	    }
	    Element element = (Element) doc.getDocumentElement().getElementsByTagName("images").item(0);
	    List<Image> images = getImagesFromElement(element);
	    cache.remove(categoryId);
	    cache.put(categoryId, images);
	    if (firstCatInCache == null) {
		firstCatInCache = categoryId;
	    }
	    return images;
	} else {
	    return cache.get(categoryId);
	}
    }

    /**
     * Creation of an image<br/>
     * Sequence : <br/>
     * <li>
     * <ul>sending of the thumbnail in base64, thanks to the method addchunk.</ul>
     * <ul>sending of the image in base64, thanks to the method addchunk</ul>
     * <ul>using of the add method to add the image to the database<ul>
     * </li>
     * Finally, the response of the webservice is checked
     *
     * @param image the image to create
     * @return true if the creation of the image was the successful
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws WrongChunkSizeException
     * @throws JiwigoException 
     * @throws Exception
     */
    //TODO ne pas continuer si une des reponses precedentes est negative
    public boolean create(Image image, Double chunkSize) throws FileAlreadyExistsException, IOException,
	    ProxyAuthenticationException, NoSuchAlgorithmException, WrongChunkSizeException, JiwigoException {
	if (exists(image)) {
	    throw new FileAlreadyExistsException("Photo already exists");
	}
	//thumbnail converted to base64
	BASE64Encoder base64 = new BASE64Encoder();

	String thumbnailBase64 = base64.encode(Tools.getBytesFromFile(image.getThumbnail()));
	//sends the thumbnail and gets the result
	Document reponseThumb = (sessionManager.executeReturnDocument(MethodsEnum.ADD_CHUNK.getLabel(), "data",
		thumbnailBase64, "type", "thumb", "position", "1", "original_sum",
		Tools.getMD5Checksum(image.getOriginale().getAbsolutePath())));

	//begin feature:0001827
	int chunk = chunkSize.intValue();
	if (chunk == 0) {
	    throw new WrongChunkSizeException("Error : the chunk size cannot be 0");
	}
	filesToSend = Tools.splitFile(image.getOriginale(), chunk);
	boolean echec = false;
	for (int i = 0; i < filesToSend.size(); i++) {
	    File fichierAEnvoyer = filesToSend.get(i);
	    String originaleBase64 = base64.encode(Tools.getBytesFromFile(fichierAEnvoyer));
	    Document reponseOriginale = (sessionManager.executeReturnDocument(MethodsEnum.ADD_CHUNK.getLabel(), "data",
		    originaleBase64, "type", "file", "position", String.valueOf(i), "original_sum",
		    Tools.getMD5Checksum(image.getOriginale().getAbsolutePath())));
	    if (!Tools.checkOk(reponseOriginale)) {
		echec = true;
		break;
	    }
	}
	//end

	//add the image in the database and get the result of the webservice
	Document reponseAjout = (sessionManager.executeReturnDocument(MethodsEnum.ADD_IMAGE.getLabel(), "file_sum",
		Tools.getMD5Checksum(image.getOriginale().getAbsolutePath()), "thumbnail_sum",
		Tools.getMD5Checksum(image.getThumbnail().getCanonicalPath()), "position", "1", "original_sum",
		Tools.getMD5Checksum(image.getOriginale().getAbsolutePath()), "categories",
		String.valueOf(image.getIdCategory()), "name", image.getName(), "author", sessionManager.getLogin(),
		"level", image.getPrivacyLevel()));
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Response add : " + Tools.documentToString(reponseAjout));
	}
	//	System.out.println(Main.sessionManager.executerReturnString("pwg.images.add", "file_sum", Outil
	//		.getMD5Checksum(image.getOriginale().getAbsolutePath()), "thumbnail_sum", Outil.getMD5Checksum(image
	//		.getThumbnail().getCanonicalPath()), "position", "1", "original_sum", Outil.getMD5Checksum(image
	//		.getOriginale().getAbsolutePath()), "categories", String.valueOf(image.getIdCategory()), "name", image
	//		.getName(), "author", Main.sessionManager.getLogin()));
	//	Document reponsePrivacy = null;
	//	if (Outil.checkOk(reponseAjout)) {
	//	    reponsePrivacy = Main.sessionManager.executerReturnDocument(MethodsEnum.SET_PRIVACY_LEVEL.getLabel());
	//	}
	boolean reussite = true;
	if (!Tools.checkOk(reponseThumb) || echec || !Tools.checkOk(reponseAjout)) {
	    reussite = false;
	}
	deleteTempFiles();
	return reussite;

    }

    /**
     * Function to test if a photo already exists
     * @param image the image to test
     * @return true if the photo exists
     * @throws JiwigoException
     */
    private boolean exists(Image image) throws JiwigoException {
	try {
	    String imageMD5 = Tools.getMD5Checksum(image.getOriginale().getAbsolutePath());
	    Document docResult = sessionManager.executeReturnDocument(MethodsEnum.IMAGE_EXIST.getLabel(),
		    "md5sum_list", imageMD5);
	    String result = Tools.getStringValueDom(docResult.getDocumentElement(), imageMD5);

	    if (result != null && !"".equals(result)) {
		return true;
	    } else {
		return false;
	    }
	} catch (NoSuchAlgorithmException e) {
	    throw new JiwigoException(e);
	} catch (IOException e) {
	    throw new JiwigoException(e);
	}
    }

    /**
     * Add tags to an image
     * @param imageId id of the image
     * @param tagId ids of the tags
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean addTags(Integer imageId, String tagId) throws JiwigoException {
	Document doc = sessionManager.executeReturnDocument(MethodsEnum.SET_INFO.getLabel(), "image_id",
		String.valueOf(imageId), "tag_ids", tagId);
	try {
	    return Tools.checkOk(doc);
	} catch (FileAlreadyExistsException e) {
	    LOG.error(Tools.getStackTrace(e));
	    return false;
	}

    }

    /**
     * parse an element to find images
     * @param element the element to parse
     * @return the list of images
     */
    private List<Image> getImagesFromElement(Element element) {
	//	List<Element> listElement = (List<Element>) element.getChildren("image");
	NodeList listImages = element.getElementsByTagName("image");
	ArrayList<Image> images = new ArrayList<Image>();
	for (int i = 0; i < listImages.getLength(); i++) {
	    Node nodeImage = listImages.item(i);
	    if (nodeImage.getNodeType() == Node.ELEMENT_NODE) {
		Element im = (Element) nodeImage;
		Image myImage = new Image();
		myImage.setUrl(im.getAttribute("element_url"));
		myImage.setWidth(Integer.valueOf(im.getAttribute("width")));
		myImage.setHeight(Integer.valueOf(im.getAttribute("height")));
		myImage.setFile(im.getAttribute("file"));
		myImage.setSeen(Integer.valueOf(im.getAttribute("hit")));
		myImage.setIdentifier(Integer.valueOf(im.getAttribute("id")));
		myImage.setName(Tools.getStringValueDom(im, "name"));
		// "derivatives" node lists alternative sizes since Piwigo 2.4
		Element elementDerivatives = (Element) im.getElementsByTagName("derivatives").item(0);
		if (elementDerivatives != null) {
			//Thumbnail is in "derivatives" node since Piwigo 2.4
			Element thumbDerivative = (Element) elementDerivatives.getElementsByTagName("thumb").item(0);
			myImage.setThumbnailUrl(Tools.getStringValueDom(thumbDerivative, "url"));
		}
		else {
			//Thumbnail before Piwigo 2.4
			myImage.setThumbnailUrl(im.getAttribute("tn_url"));
		}
		//Resized URL : derivative with the biggest size
		myImage.setResizedUrl(findResizedUrl(elementDerivatives));

		Element elementCategories = (Element) im.getElementsByTagName("categories").item(0);
		if (elementCategories != null) {
		    Element elementCategory = (Element) elementCategories.getElementsByTagName("category").item(0);
		    myImage.setIdCategory(Integer.valueOf(elementCategory.getAttribute("id")));
		    if (myImage.getName() == null) {
			myImage.setName(myImage.getFile());
		    }
		}
		images.add(myImage);
	    }
	}
	return images;
    }

	/**
	 * Find Resized Url from derivative with biggest size
	 * @param elementDerivatives the list of derivatives
	 * @return the Resized Url
	 */
	private String findResizedUrl(Element elementDerivatives) {
		String resizedUrl = null;
		if (elementDerivatives != null) {
			NodeList listDerivatives = elementDerivatives.getChildNodes();
			int maxWidth = 0;
			Element biggestDerivative = null;
			for (int j = 0; j < listDerivatives.getLength(); j++) {
				Node nodeDerivative = listDerivatives.item(j);
				if (nodeDerivative.getNodeType() == Node.ELEMENT_NODE) {
					Element derivative = (Element) nodeDerivative;
					int curWidth = Integer.valueOf(Tools.getStringValueDom(derivative, "width"));
					if (curWidth > maxWidth) {
						maxWidth = curWidth;
						biggestDerivative = derivative;
					}
				}
			}
			if (biggestDerivative != null) {
				resizedUrl = Tools.getStringValueDom(biggestDerivative, "url");
			}
		}
		return resizedUrl;
	}

    /**
     * Search images
     * @param searchString the string to search
     * @return the list of images matching the string
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> search(String searchString) throws JiwigoException {
	Document doc = sessionManager.executeReturnDocument(MethodsEnum.SEARCH.getLabel(), "query", searchString);
	LOG.debug(Tools.documentToString(doc));
	Element element = (Element) doc.getDocumentElement().getElementsByTagName("images").item(0);
	return getImagesFromElement(element);

    }

    private void deleteTempFiles() {
	File file = new File(System.getProperty("java.io.tmpdir") + "/originale.jpg");
	file.delete();
	file = new File(System.getProperty("java.io.tmpdir") + "/thumb.jpg");
	file.delete();
	for (File tempCut : filesToSend) {
	    tempCut.delete();
	}

    }

    public void addSimple(File file, Integer category, String title, Integer level) throws JiwigoException {
	HttpPost httpMethod = new HttpPost(((SessionManagerImpl) sessionManager).getUrl());

	//	nameValuePairs.add(new BasicNameValuePair("method", "pwg.images.addSimple"));
	//	for (int i = 0; i < parametres.length; i += 2) {
	//	    nameValuePairs.add(new BasicNameValuePair(parametres[i], parametres[i + 1]));
	//	}
	//	method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	if (file != null) {
	    MultipartEntity multipartEntity = new MultipartEntity();

	    //		String string = nameValuePairs.toString();
	    // dirty fix to remove the enclosing entity{}
	    //		String substring = string.substring(string.indexOf("{"),
	    //				string.lastIndexOf("}") + 1);
	    try {
		multipartEntity.addPart("method", new StringBody(MethodsEnum.ADD_SIMPLE.getLabel()));
		multipartEntity.addPart("category", new StringBody(category.toString()));
		multipartEntity.addPart("name", new StringBody(title));
		if (level != null) {
		    multipartEntity.addPart("level", new StringBody(level.toString()));
		}
	    } catch (UnsupportedEncodingException e) {
		throw new JiwigoException(e);
	    }

	    //		StringBody contentBody = new StringBody(substring,
	    //				Charset.forName("UTF-8"));
	    //		multipartEntity.addPart("entity", contentBody);
	    FileBody fileBody = new FileBody(file);
	    multipartEntity.addPart("image", fileBody);
	    ((HttpPost) httpMethod).setEntity(multipartEntity);
	}

	HttpResponse response;
	StringBuilder sb = new StringBuilder();
	try {
	    response = ((SessionManagerImpl) sessionManager).getClient().execute(httpMethod);

	    int responseStatusCode = response.getStatusLine().getStatusCode();

	    switch (responseStatusCode) {
	    case HttpURLConnection.HTTP_CREATED:
		break;
	    case HttpURLConnection.HTTP_OK:
		break;
	    case HttpURLConnection.HTTP_BAD_REQUEST:
		throw new JiwigoException("status code was : " + responseStatusCode);
	    case HttpURLConnection.HTTP_FORBIDDEN:
		throw new JiwigoException("status code was : " + responseStatusCode);
	    case HttpURLConnection.HTTP_NOT_FOUND:
		throw new JiwigoException("status code was : " + responseStatusCode);
	    default:
		throw new JiwigoException("status code was : " + responseStatusCode);
	    }

	    HttpEntity resultEntity = response.getEntity();
	    BufferedHttpEntity responseEntity = new BufferedHttpEntity(resultEntity);

	    BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
	    String line;

	    try {
		while ((line = reader.readLine()) != null) {
		    sb.append(line);
		    sb.append("\n");
		}
	    } finally {
		reader.close();
	    }
	} catch (ClientProtocolException e) {
	    throw new JiwigoException(e);
	} catch (IOException e) {
	    throw new JiwigoException(e);
	}
	String stringResult = sb.toString();

    }

    /** 
     * @see fr.mael.jiwigo.dao.ImageDao#delete(fr.mael.jiwigo.om.Image)
     */
    public boolean delete(Image image) throws JiwigoException {
	String pwgToken = sessionManager.getPwgToken();
	if (pwgToken == null) {
	    throw new JiwigoException("Error : received a null pwg_token");
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Deletes image " + image.getIdentifier() + " with pwg_token = " + pwgToken);
	}

	Document doc = sessionManager.executeReturnDocument(MethodsEnum.DELETE_IMAGE.getLabel(), "image_id",
		String.valueOf(image.getIdentifier()), "pwg_token", pwgToken);

	if (LOG.isDebugEnabled()) {
	    LOG.debug(Tools.documentToString(doc));
	}
	try {
	    return Tools.checkOk(doc);
	} catch (FileAlreadyExistsException e) {
	    e.printStackTrace();
	}
	return false;

    }

    public SessionManager getSessionManager() {
	return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
	this.sessionManager = sessionManager;
    }

}

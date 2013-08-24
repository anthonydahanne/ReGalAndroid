package fr.mael.jiwigo.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.mael.jiwigo.dao.TagDao;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.om.Tag;
import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.enumeration.TagEnum;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.session.SessionManager;
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
 * Dao of the categories
 * @author mael
 *
 */
public class TagDaoImpl implements TagDao {
    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(ImageDaoImpl.class);

    private SessionManager sessionManager;

    /**
     * lists the tags
     * @return the list of tags
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Tag> list() throws JiwigoException {
	Document doc = sessionManager.executeReturnDocument(MethodsEnum.TAGS_ADMIN_LIST.getLabel());
	return getTagsFromDocument((Element) doc.getDocumentElement().getElementsByTagName("tags").item(0));

    }

    /**
     * COnstructs a list of tags from a document
     * @param doc the document
     * @return the list of tags
     */
    private List<Tag> getTagsFromDocument(Element element) {
	NodeList listTags = element.getElementsByTagName("tag");

	//	List<Element> listElement = (List<Element>) element.getChildren("tag");
	ArrayList<Tag> tags = new ArrayList<Tag>();
	for (int i = 0; i < listTags.getLength(); i++) {
	    Node nodeTag = listTags.item(0);
	    if (nodeTag.getNodeType() == Node.ELEMENT_NODE) {
		Element tagElement = (Element) nodeTag;
		Tag tag = new Tag();
		tag.setIdentifier(Integer.valueOf(tagElement.getAttribute(TagEnum.ID.getLabel())));
		tag.setName(tagElement.getAttribute(TagEnum.NAME.getLabel()));
		tags.add(tag);
	    }
	}
	return tags;

    }

    /**
     * Creation of a tag
     * @param tag the tag to create
     * @return true if the tag as been successfully created
     * @throws ProxyAuthenticationException
     */
    public boolean create(Tag tag) throws JiwigoException {
	try {
	    return Tools.checkOk(sessionManager.executeReturnDocument(MethodsEnum.ADD_TAG.getLabel(), "name",
		    tag.getName()));
	} catch (FileAlreadyExistsException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	}
    }

    /**
     * Function that returns the tags for an image
     * @param image the image
     * @return the tags list
     * @throws IOException
     * @throws JiwigoException
     */
    public List<Tag> tagsForImage(Image image) throws JiwigoException {
	Document doc = sessionManager.executeReturnDocument(MethodsEnum.GET_INFO.getLabel(), "image_id",
		String.valueOf(image.getIdentifier()));
	Element elementImage = (Element) doc.getDocumentElement().getElementsByTagName("image").item(0);
	Element elementTag = (Element) elementImage.getElementsByTagName("tags").item(0);
	return getTagsFromDocument(elementTag);
    }

    public SessionManager getSessionManager() {
	return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
	this.sessionManager = sessionManager;
    }

}

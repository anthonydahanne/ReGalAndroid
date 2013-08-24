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

import fr.mael.jiwigo.dao.CommentDao;
import fr.mael.jiwigo.om.Comment;
import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
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

 * Dao for the comments
 * @author mael
 *
 */
public class CommentDaoImpl implements CommentDao {
    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(CommentDaoImpl.class);

    private SessionManager sessionManager;

    /**
     * Listing of the comments for the given image
     * @param idImage id of the  image
     * @return list of comments
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Comment> list(Integer idImage) throws JiwigoException {
	Document doc = (sessionManager.executeReturnDocument(MethodsEnum.GET_INFO.getLabel(), "image_id",
		String.valueOf(idImage), "comments_per_page", "100"));
	Element elementImage = (Element) doc.getDocumentElement().getElementsByTagName("image").item(0);
	Element elementComments = (Element) elementImage.getElementsByTagName("comments").item(0);
	NodeList listComments = elementComments.getElementsByTagName("comment");
	ArrayList<Comment> comments = new ArrayList<Comment>();
	for (int i = 0; i < listComments.getLength(); i++) {
	    Node nodeCom = listComments.item(i);
	    if (nodeCom.getNodeType() == Node.ELEMENT_NODE) {
		Element com = (Element) nodeCom;
		Comment myCom = new Comment();
		myCom.setIdentifier(Integer.valueOf(com.getAttribute("id")));
		myCom.setDate(com.getAttribute("date"));
		myCom.setAuthor(Tools.getStringValueDom(com, "author"));
		myCom.setContent(Tools.getStringValueDom(com, "content"));
		comments.add(myCom);
	    }
	}
	return comments;
    }

    /**
     * Gets the key essential to add a comment
     * @param idImage the id of the image
     * @return the key
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public String getKey(Integer idImage) throws JiwigoException {
	Document doc = (sessionManager.executeReturnDocument(MethodsEnum.GET_INFO.getLabel(), "image_id",
		String.valueOf(idImage)));
	//	String key = doc.getRootElement().getChild("image").getChild("comment_post").getAttributeValue("key");
	Element elementImage = (Element) doc.getDocumentElement().getElementsByTagName("image").item(0);
	Element elementCommentPost = (Element) elementImage.getElementsByTagName("comment_post").item(0);
	return elementCommentPost.getAttribute("key");
    }

    /**
     * Creates a comment
     * @param commentaire the comment to add
     * @return true if the comment is successfully added
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean create(Comment commentaire) throws JiwigoException {
	String key = getKey(commentaire.getImageId());
	try {
	    Thread.sleep(2200);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	try {
	    return Tools.checkOk(sessionManager.executeReturnDocument(MethodsEnum.AJOUTER_COMMENTAIRE.getLabel(),
		    "image_id", String.valueOf(commentaire.getImageId()), "author", commentaire.getAuthor(), "content",
		    commentaire.getContent(), "key", key));
	} catch (FileAlreadyExistsException e) {
	    LOG.error(Tools.getStackTrace(e));
	    return false;
	}

    }

    public SessionManager getSessionManager() {
	return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
	this.sessionManager = sessionManager;
    }

}

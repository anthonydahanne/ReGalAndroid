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

import fr.mael.jiwigo.dao.CategoryDao;
import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.transverse.enumeration.CategoryEnum;
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
 * Dao for the categories
 * @author mael
 *
 */
public class CategoryDaoImpl implements CategoryDao {
    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(CategoryDaoImpl.class);
    /**
     * Instance to use a singleton
     */
    private static CategoryDaoImpl instance;

    private SessionManager sessionManager;

    /**
     * Lists the categories
     * @param recursive true : recursive listing of the categories
     * @return the list of categories
     * @throws JiwigoException 
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Category> list(boolean recursive) throws JiwigoException {
	Document doc = sessionManager.executeReturnDocument(MethodsEnum.LISTER_CATEGORIES.getLabel(), "recursive",
		String.valueOf(recursive));
	Element element = (Element) doc.getDocumentElement().getElementsByTagName("categories").item(0);
	NodeList nodeList = element.getElementsByTagName("category");
	ArrayList<Category> categories = new ArrayList<Category>();
	for (int i = 0; i < nodeList.getLength(); i++) {
	    Node catNode = nodeList.item(i);
	    if (catNode.getNodeType() == Node.ELEMENT_NODE) {
		Element cat = (Element) catNode;
		Category myCat = new Category();
		myCat.setIdentifier(Integer.valueOf(cat.getAttribute(CategoryEnum.ID.getLabel())));
		myCat.setUrlCategory(cat.getAttribute(CategoryEnum.URL.getLabel()));
		myCat.setNbImages(Integer.valueOf(cat.getAttribute(CategoryEnum.NB_IMAGES.getLabel())));
		myCat.setNbTotalImages(Integer.valueOf(cat.getAttribute(CategoryEnum.NB_TOTAL_IMAGES.getLabel())));
		myCat.setName(Tools.getStringValueDom(cat, CategoryEnum.NAME.getLabel()));
		String catMeres = Tools.getStringValueDom(cat, CategoryEnum.CAT_MERES.getLabel());
		ArrayList<Integer> idCategoriesMeres = new ArrayList<Integer>();
		myCat.setIdParentCategoriesString(catMeres);
		for (String catA : catMeres.split(",")) {
		    if (!catA.equals("")) {
			idCategoriesMeres.add(Integer.valueOf(catA));
		    }
		}
		myCat.setIdParentCategories(idCategoriesMeres);
			myCat.setUrlThumbnail(Tools.getStringValueDom(cat, CategoryEnum.URL_THUMBNAIL.getLabel()));
		categories.add(myCat);
	    }
	}
	return categories;
    }

    /**
     * Creation of a category
     * @param category the category to create
     * @return true if the category is successfully created
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public boolean create(Category category) throws JiwigoException {
	try {
	    if (category.getDirectParent() != null) {
		return Tools.checkOk(sessionManager.executeReturnDocument(MethodsEnum.AJOUTER_CATEGORIE.getLabel(),
			"name", category.getName(), "parent", String.valueOf(category.getDirectParent())));
	    } else {
		return Tools.checkOk(sessionManager.executeReturnDocument(MethodsEnum.AJOUTER_CATEGORIE.getLabel(),
			"name", category.getName()));
	    }
	} catch (FileAlreadyExistsException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	}
    }

    /**
     * @see fr.mael.jiwigo.dao.CategoryDao#delete(fr.mael.jiwigo.om.Category)
     */
    public boolean delete(Category category) throws JiwigoException {
	String pwgToken = sessionManager.getPwgToken();
	if (pwgToken == null) {
	    throw new JiwigoException("Error : received a null pwg_token");
	}
	if (LOG.isDebugEnabled()) {
	    LOG.debug("Deletes category " + category.getIdentifier() + " with pwg_token = " + pwgToken);
	}

	Document doc = sessionManager.executeReturnDocument(MethodsEnum.DELETE_CATEGORY.getLabel(), "category_id",
		String.valueOf(category.getIdentifier()), "pwg_token", pwgToken);

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

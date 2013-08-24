package fr.mael.jiwigo.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.dao.CategoryDao;
import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.service.CategoryService;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;

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
public class CategoryServiceImpl implements CategoryService {
    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private CategoryDao dao;

    /**
     * Lists all categories
     * @param recursive true : recursive listing
     * @return the list of categories
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Category> list(boolean recursive) throws JiwigoException {
	return dao.list(recursive);
    }

    /**
     * Allows to create the categories tree
     * @return list of categories
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public List<Category> makeTree() throws JiwigoException {
	List<Category> list = dao.list(true);
	for (Category category : list) {
	    for (Category category2 : list) {
		if (category2.getIdParentCategories().size() != 1
			&& category.getIdentifier().equals(
				category2.getIdParentCategories().get(category2.getIdParentCategories().size() - 2))) {
		    category.getChildCategories().add(category2);
		    category2.getParentCategories().add(category);
		}

	    }
	}

	return list;

    }

    /**
     * creation of a category
     * @param nom name of the category
     * @param parent parent category
     * @return true if successful
     * @throws ProxyAuthenticationException
     */
    public boolean create(String nom, Integer parent) throws JiwigoException {
	Category category = new Category();
	category.setDirectParent(parent);
	category.setName(nom);
	return dao.create(category);
    }

    /**
     * creation of a category without parent
     * @param nom name of the category
     * @return true if successful
     * @throws ProxyAuthenticationException
     */
    public boolean create(String nom) throws JiwigoException {
	Category category = new Category();
	category.setName(nom);
	return dao.create(category);
    }

    public CategoryDao getDao() {
	return dao;
    }

    public void setDao(CategoryDao dao) {
	this.dao = dao;
    }

    public boolean delete(Category category) throws JiwigoException {
	if (category.getIdentifier() == null) {
	    throw new JiwigoException("The identifier of the category cannot be null");
	} else {
	    return dao.delete(category);
	}
    }

}

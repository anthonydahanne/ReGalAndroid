package fr.mael.jiwigo.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.dao.TagDao;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.om.Tag;
import fr.mael.jiwigo.service.TagService;
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
 *

 * @author mael
 *
 */
public class TagServiceImpl implements TagService {
    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    private TagDao dao;

    /**
     * Lists all tags
     * @return le list of tags
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public List<Tag> list() throws JiwigoException {
	return dao.list();
    }

    /**
     * Creates a tag
     * @param nom name of the tag
     * @return true if the tag is created
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public boolean create(String nom) throws JiwigoException {
	Tag tag = new Tag();
	tag.setName(nom);
	return dao.create(tag);
    }

    /**
     * Returns all the tag for an image
     * @param image the image to check
     * @return the list of tags
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public List<Tag> tagsForImage(Image image) throws JiwigoException {
	return dao.tagsForImage(image);
    }

    public TagDao getDao() {
	return dao;
    }

    public void setDao(TagDao dao) {
	this.dao = dao;
    }

}

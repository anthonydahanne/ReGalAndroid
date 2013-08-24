package fr.mael.jiwigo.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.dao.CommentDao;
import fr.mael.jiwigo.om.Comment;
import fr.mael.jiwigo.service.CommentService;
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
public class CommentServiceImpl implements CommentService {
    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentDao dao;

    /**
     * Lists all comments for an image
     * @param imageId the id of the image
     * @return the list of comments
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Comment> list(Integer imageId) throws JiwigoException {
	return dao.list(imageId);
    }

    /**
     * Creates a comment for an image
     * @param content the comment
     * @param imageId the id of the image
     * @param auteur the author of the comment
     * @return true if successful
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean create(String content, Integer imageId, String auteur) throws JiwigoException {
	Comment comment = new Comment();
	comment.setContent(content);
	comment.setImageId(imageId);
	comment.setAuthor(auteur);
	return dao.create(comment);
    }

    public CommentDao getDao() {
	return dao;
    }

    public void setDao(CommentDao dao) {
	this.dao = dao;
    }

}

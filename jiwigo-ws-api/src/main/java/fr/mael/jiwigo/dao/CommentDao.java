package fr.mael.jiwigo.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import fr.mael.jiwigo.om.Comment;
import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.util.Outil;

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
   
 * Dao for the comments
 * @author mael
 *
 */
public class CommentDao extends DaoBase {
    /**
     * Logger
     */
    public static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory
	    .getLog(CommentDao.class);
    /**
     *  Instance that allows to use a singleton
     */
    private static CommentDao instance;

    /**
     * private constructor, to use a singleton
     */
    private CommentDao() {

    }

    /**
     * @return the singleton
     */
    public static CommentDao getInstance(SessionManager sessionManager) {
	if (instance == null) {
	    instance = new CommentDao();
	    setSessionManager(sessionManager);
	}
	return instance;
    }

    /**
     * Listing of the comments for the given image
     * @param idImage id of the  image
     * @return list of comments
     * @throws IOException 
     */
    public List<Comment> lister(Integer idImage) throws IOException {
	Document doc = (getSessionManager().executerReturnDocument(MethodsEnum.GET_INFO.getLabel(), "image_id", String
		.valueOf(idImage)));
	Element element = doc.getRootElement().getChild("image").getChild("comments");
	List<Element> listElement = (List<Element>) element.getChildren("comment");
	ArrayList<Comment> comments = new ArrayList<Comment>();
	for (Element com : listElement) {
	    Comment myCom = new Comment();
	    myCom.setIdentifiant(Integer.valueOf(com.getAttributeValue("id")));
	    myCom.setDate(com.getAttributeValue("date"));
	    myCom.setAuthor(com.getChildText("author"));
	    myCom.setContent(com.getChildText("content"));
	    comments.add(myCom);
	}
	return comments;
    }

    /**
     * Gets the key essential to add a comment
     * @param idImage the id of the image
     * @return the key
     * @throws IOException 
     */
    public String getKey(Integer idImage) throws IOException {
	Document doc = (getSessionManager().executerReturnDocument(MethodsEnum.GET_INFO.getLabel(), "image_id", String
		.valueOf(idImage)));
	String key = doc.getRootElement().getChild("image").getChild("comment_post").getAttributeValue("key");
	return key;
    }

    /**
     * Creates a comment
     * @param commentaire the comment to add
     * @return true if the comment is successfully added
     * @throws IOException
     */
    public boolean creer(Comment commentaire) throws IOException {
	String key = getKey(commentaire.getImageId());
	try {
	    Thread.sleep(2200);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	return Outil.checkOk(getSessionManager().executerReturnDocument(MethodsEnum.AJOUTER_COMMENTAIRE.getLabel(),
		"image_id", String.valueOf(commentaire.getImageId()), "author", commentaire.getAuthor(), "content",
		commentaire.getContent(), "key", key));

    }
}

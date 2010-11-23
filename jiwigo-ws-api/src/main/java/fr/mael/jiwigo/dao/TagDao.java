package fr.mael.jiwigo.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.om.Tag;
import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.enumeration.TagEnum;
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
   
 * Dao des catégories
 * @author mael
 *
 */
public class TagDao extends DaoBase {
    /**
     * Logger
     */
	 private  final Logger LOG = LoggerFactory.getLogger(TagDao.class);
	/**
     * Instance, to use a singleton
     */
    private static TagDao instance;

    /**
     * private constructor to use a singleton
     */
    private TagDao() {

    }

    /**
     * @return the instance
     */
    public static TagDao getInstance(SessionManager sessionManager) {
	if (instance == null) {
	    instance = new TagDao();
	    setSessionManager(sessionManager);
	}
	return instance;
    }

    /**
     * lists the tags
     * @return the list of tags
     * @throws IOException 
     */
    public List<Tag> lister() throws IOException {
	Document doc = getSessionManager().executerReturnDocument(MethodsEnum.TAGS_ADMIN_LIST.getLabel());
	//	System.out.println(Outil.documentToString(doc));
	return getTagsFromDocument(doc.getRootElement().getChild("tags"));

    }

    /**
     * COnstructs a list of tags from a document
     * @param doc the document
     * @return the list of tags
     */
    private List<Tag> getTagsFromDocument(Element element) {

	List<Element> listElement = element.getChildren("tag");
	ArrayList<Tag> tags = new ArrayList<Tag>();
	for (Element tagElement : listElement) {
	    Tag tag = new Tag();
	    tag.setId(Integer.valueOf(tagElement.getAttributeValue(TagEnum.ID.getLabel())));
	    tag.setNom(tagElement.getAttributeValue(TagEnum.NAME.getLabel()));
	    tags.add(tag);
	}
	return tags;

    }

    /**
     * Creation of a tag
     * @param tag the tag to create
     * @return true if the tag as been successfully created
     */
    public boolean creer(Tag tag) {
	try {
	    return Outil.checkOk(getSessionManager().executerReturnDocument(MethodsEnum.ADD_TAG.getLabel(), "name",
		    tag.getNom()));
	} catch (IOException e) {
	    LOG.error(Outil.getStackTrace(e));
	}
	return false;
    }

    /**
     * Function that returns the tags for an image
     * @param image the image
     * @return the tags list
     * @throws IOException 
     */
    public List<Tag> tagsForImage(Image image) throws IOException {
	Document doc = getSessionManager().executerReturnDocument(MethodsEnum.GET_INFO.getLabel(), "image_id",
		String.valueOf(image.getIdentifiant()));
	return getTagsFromDocument(doc.getRootElement().getChild("image").getChild("tags"));
    }

}

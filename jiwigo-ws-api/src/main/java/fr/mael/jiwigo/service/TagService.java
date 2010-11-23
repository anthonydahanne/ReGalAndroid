package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.dao.TagDao;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.om.Tag;
import fr.mael.jiwigo.transverse.session.SessionManager;

/**
 * 
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
public class TagService extends ServiceBase {
    /**
     * Logger
     */
    public static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory
	    .getLog(TagService.class);

    /**
     * The instance, to use a singleton
     */
    private static TagService instance;

    /**
     * @return the singleton
     */
    public static TagService getInstance(SessionManager sessionManager) {
	if (instance == null) {
	    instance = new TagService();
	    setSessionManager(sessionManager);
	}
	return instance;
    }

    /**
     * private constructor
     */
    private TagService() {

    }

    /**
     * Lists all tags
     * @return le list of tags
     * @throws IOException
     */
    public List<Tag> lister() throws IOException {
	return TagDao.getInstance(getSessionManager()).lister();
    }

    /**
     * Creates a tag
     * @param nom name of the tag
     * @return true if the tag is created
     * @throws IOException
     */
    public boolean creer(String nom) throws IOException {
	Tag tag = new Tag();
	tag.setNom(nom);
	return TagDao.getInstance(getSessionManager()).creer(tag);
    }

    /**
     * Returns all the tag for an image
     * @param image the image to check
     * @return the list of tags
     * @throws IOException
     */
    public List<Tag> tagsForImage(Image image) throws IOException {
	return TagDao.getInstance(getSessionManager()).tagsForImage(image);
    }

}

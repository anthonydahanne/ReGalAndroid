package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.dao.CommentDao;
import fr.mael.jiwigo.om.Comment;
import fr.mael.jiwigo.transverse.session.SessionManager;

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
   
 * Service des commentaires
 * @author mael
 *
 */
public class CommentService extends ServiceBase {
    /**
     * Logger
     * @param sessionManager 
     */
//	private  final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    public CommentService(SessionManager sessionManager) {
    	setSessionManager(sessionManager);

    }

    /**
     * Lists all comments for an image
     * @param imageId the id of the image
     * @return the list of comments
     * @throws IOException 
     */
    public List<Comment> lister(Integer imageId) throws IOException {
	return CommentDao.getInstance(getSessionManager()).lister(imageId);
    }

    /**
     * Creates a comment for an image
     * @param content the comment
     * @param imageId the id of the image
     * @param auteur the author of the comment
     * @return true if successful
     * @throws IOException
     */
    public boolean creer(String content, Integer imageId, String auteur) throws IOException {
	Comment commentaire = new Comment();
	commentaire.setContent(content);
	commentaire.setImageId(imageId);
	commentaire.setAuthor(auteur);
	return CommentDao.getInstance(getSessionManager()).creer(commentaire);
    }

}

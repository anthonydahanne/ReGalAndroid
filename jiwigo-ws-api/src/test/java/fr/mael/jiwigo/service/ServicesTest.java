package fr.mael.jiwigo.service;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.transverse.session.SessionManager;

public class ServicesTest{

   private SessionManager sessionManager;

	@Before
	public void setUp(){
	    	sessionManager =  new SessionManager("mael", "motdepasse", "http://mael.piwigo.com");
	    	sessionManager.processLogin();
	}
    

    @Test
    public void testCreer() throws Exception {
	Category cat = null;
	for (Category category : CategoryService.getInstance(sessionManager).lister(true)) {
	    if (category.getIdentifiant().equals(3)) {
		cat = category;
		break;
	    }
	}
	Image image = ImageService.getInstance(sessionManager).listerParCategory(cat.getIdentifiant(), true).get(0);
	int firstCount = CommentService.getInstance(sessionManager).lister(image.getIdentifiant()).size();
	CommentService.getInstance(sessionManager).creer("comment test", image.getIdentifiant(), "none");
	Assert.assertSame(firstCount + 1, CommentService.getInstance(sessionManager).lister(image.getIdentifiant()).size());
    }
}

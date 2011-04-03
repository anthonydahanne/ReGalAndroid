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
	    	sessionManager =  new SessionManager("mael", "motdepasse", "http://mael.piwigo.com", "Unit Test");
	    	sessionManager.processLogin();
	}
    

    @Test
    public void testCreer() throws Exception {
		Category cat = null;
		CategoryService categoryService = new CategoryService(sessionManager);
		ImageService imageService = new ImageService(sessionManager);
		CommentService commentService = new CommentService(sessionManager);
		
		for (Category category : categoryService.lister(true)) {
		    if (category.getIdentifiant().equals(3)) {
			cat = category;
			break;
		    }
		}
		Image image = imageService.listerParCategory(cat.getIdentifiant(), true).get(0);
		Assert.assertEquals("test de test do not delete",cat.getNom());
		Assert.assertEquals("20110329194915-0c0b3f36.jpg",image.getFile());
	
    }
}

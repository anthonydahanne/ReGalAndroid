package fr.mael.jiwigo.service;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.mael.jiwigo.dao.impl.CategoryDaoImpl;
import fr.mael.jiwigo.dao.impl.ImageDaoImpl;
import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.service.impl.CategoryServiceImpl;
import fr.mael.jiwigo.service.impl.ImageServiceImpl;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.session.impl.SessionManagerImpl;

@Ignore
public class ServicesTest {

    private SessionManager sessionManager;

    @Before
    public void setUp() throws JiwigoException {
	sessionManager = new SessionManagerImpl("mael", "motdepasse", "http://mael.piwigo.com", "Unit Test");
	sessionManager.processLogin();
    }

    @Test
    public void testCreer() throws Exception {
	Category cat = null;
	CategoryServiceImpl categoryService = new CategoryServiceImpl();
	CategoryDaoImpl categoryDao = new CategoryDaoImpl();
	categoryDao.setSessionManager(sessionManager);
	categoryService.setDao(categoryDao);

	ImageServiceImpl imageService = new ImageServiceImpl();
	ImageDaoImpl imageDao = new ImageDaoImpl();
	imageDao.setSessionManager(sessionManager);
	imageService.setDao(imageDao);
	//we choose category number 3
	for (Category category : categoryService.list(true)) {
	    if (category.getIdentifier().equals(3)) {
		cat = category;
		break;
	    }
	}
	//we look for the first picture of category 3
	Image image = imageService.listByCategory(cat.getIdentifier(), true).get(0);
	Assert.assertEquals("test de test do not delete", cat.getName());
	Assert.assertEquals("20110329194915-0c0b3f36.jpg", image.getFile());

    }

    @Test
    public void addSimpleTest() {

	// we prepare the service implementing add-simple
	ImageServiceImpl imageService = new ImageServiceImpl();
	ImageDaoImpl dao = new ImageDaoImpl();
	dao.setSessionManager(sessionManager);
	imageService.setDao(dao);

	//we prepare the resource to send
	File imageFile = new File(this.getClass().getClassLoader().getResource("piwigo_org.png").getPath());
	//	File imageFile = new File("piwigo_org.png");
	Integer categoryTest = 98;
	String title = "title" + System.currentTimeMillis();
	try {
	    imageService.addSimple(imageFile, categoryTest, title);
	} catch (JiwigoException e) {
	    Assert.fail("An exception was thrown while trying to send the pictures" + e.getMessage());
	}

	//the image should be sent, let's check if piwigo added it to the category.
	boolean found = false;
	List<Image> listByCategory;
	try {
	    listByCategory = imageService.listByCategory(categoryTest, true);
	    for (Image image : listByCategory) {
		if (image.getName().equals(title)) {
		    found = true;
		    break;
		}
	    }
	} catch (Exception e) {
	    Assert.fail("An exception was thrown while trying to fetch the pictures from category " + categoryTest
		    + "exception was : " + e.getMessage());
	}
	Assert.assertTrue("The picture just sent could not be found", found);

    }
}

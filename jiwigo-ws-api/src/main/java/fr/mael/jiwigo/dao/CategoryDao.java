package fr.mael.jiwigo.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.transverse.enumeration.CategoryEnum;
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
   
 * Dao des catégories
 * @author mael
 *
 */
public class CategoryDao extends DaoBase{
    /**
     * Logger
     */
    private  final Logger LOG = LoggerFactory
	    .getLogger(CategoryDao.class);

    /**
     * Private constructor to use a singleton
     */
    public CategoryDao(SessionManager sessionManager) {
    	setSessionManager(sessionManager);

    }


    /**
     * Lists the categories
     * @param rafraichir true : uses the categories of the cache
     * @param recursive true : recursive listing of the categories 
     * @return the list of categories
     * @throws IOException 
     */
    public List<Category> listing(boolean recursive) throws IOException {
	Document doc = getSessionManager().executerReturnDocument(MethodsEnum.LISTER_CATEGORIES.getLabel(),
		"recursive", String.valueOf(recursive));
	Element element = doc.getRootElement().getChild("categories");
	List<Element> listElement = element.getChildren("category");
	ArrayList<Category> categories = new ArrayList<Category>();
	for (Element cat : listElement) {
	    Category myCat = new Category();
	    myCat.setIdentifiant(Integer.valueOf(cat.getAttributeValue(CategoryEnum.ID.getLabel())));
	    myCat.setUrlCategory(cat.getAttributeValue(CategoryEnum.URL.getLabel()));
	    myCat.setNbImages(Integer.valueOf(cat.getAttributeValue(CategoryEnum.NB_IMAGES.getLabel())));
	    myCat.setNbTotalImages(Integer.valueOf(cat.getAttributeValue(CategoryEnum.NB_TOTAL_IMAGES.getLabel())));
	    myCat.setNom(cat.getChildText(CategoryEnum.NAME.getLabel()));
	    String catMeres = cat.getChildText(CategoryEnum.CAT_MERES.getLabel());
	    ArrayList<Integer> idCategoriesMeres = new ArrayList<Integer>();
	    for (String catA : catMeres.split(",")) {
		if (!catA.equals("")) {
		    idCategoriesMeres.add(Integer.valueOf(catA));
		}
	    }
	    myCat.setIdCategoriesMeres(idCategoriesMeres);
	    categories.add(myCat);
	}
	return categories;
    }

    /**
     * Creation of a category
     * @param category the category to create
     * @return true if the category is successfully created
     */
    public boolean create(Category category) {
	try {
	    if (category.getParentDirect() != null) {
		return Outil.checkOk(getSessionManager().executerReturnDocument(
			MethodsEnum.AJOUTER_CATEGORIE.getLabel(), "name", category.getNom(), "parent",
			String.valueOf(category.getParentDirect())));
	    } else {
		return Outil.checkOk(getSessionManager().executerReturnDocument(
			MethodsEnum.AJOUTER_CATEGORIE.getLabel(), "name", category.getNom()));
	    }
	} catch (IOException e) {
	    LOG.error(Outil.getStackTrace(e));
	}
	return false;
    }

}

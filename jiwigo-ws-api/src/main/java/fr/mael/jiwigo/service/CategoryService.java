package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.dao.CategoryDao;
import fr.mael.jiwigo.om.Category;
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
   
 * Service des catégories
 * @author mael
 *
 */
public class CategoryService extends ServiceBase {
    /**
     * Logger
     */
    public static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory
	    .getLog(CategoryService.class);

    /**
     * singleton
     */
    private static CategoryService instance;

    /**
     * @return the singleton
     */
    public static CategoryService getInstance(SessionManager sessionManager) {
	if (instance == null) {
	    instance = new CategoryService();
	    setSessionManager(sessionManager);
	}
	return instance;
    }

    /**
     * private constructor to use a singleton
     */
    private CategoryService() {

    }

    /**
     * Lists all categories
     * @param rafraichir true to refresh
     * @param recursive true : recursive listing
     * @return the list of categories
     * @throws IOException 
     */
    public List<Category> lister(boolean recursive) throws IOException {
	return CategoryDao.getInstance(getSessionManager()).listing(recursive);
    }

    /**
     * Allows to create the categories tree
     * @return list of categories
     * @throws IOException 
     */
    public List<Category> construireArbre() throws IOException {
	List<Category> list = CategoryDao.getInstance(getSessionManager()).listing(true);
	for (Category category : list) {
	    for (Category category2 : list) {
		if (category2.getIdCategoriesMeres().size() != 1
			&& category.getIdentifiant().equals(
				category2.getIdCategoriesMeres().get(category2.getIdCategoriesMeres().size() - 2))) {
		    category.getCategoriesFilles().add(category2);
		    category2.getCategoriesMeres().add(category);
		}

	    }
	}

	return list;

    }

    /**
     * creation of a category
     * @param nom name of the category
     * @param parent parent category
     * @return true if successful
     */
    public boolean creer(String nom, Integer parent) {
	Category category = new Category();
	category.setParentDirect(parent);
	category.setNom(nom);
	return CategoryDao.getInstance(getSessionManager()).create(category);
    }

    /**
     * creation of a category without parent
     * @param nom name of the category
     * @return true if successful
     */
    public boolean creer(String nom) {
	Category category = new Category();
	category.setNom(nom);
	return CategoryDao.getInstance(getSessionManager()).create(category);
    }
}

package fr.mael.jiwigo.om;

import java.util.ArrayList;

import org.jdom.Element;

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
   
 * @author mael
 *
 */
public class Category {

    private ArrayList<Integer> idCategoriesMeres;
    private Integer identifiant;
    private String urlCategory;
    private Integer nbImages;
    private Integer nbTotalImages;
    private ArrayList<Category> categoriesFilles;
    private ArrayList<Category> categoriesMeres;
    private String nom;
    private Element element;
    private Integer parentDirect;

    public Category() {
	categoriesFilles = new ArrayList<Category>();
	categoriesMeres = new ArrayList<Category>();
    }

    @Override
    public String toString() {
	return nom;
    }

    /**
     * @return the idCategoriesMeres
     */
    public ArrayList<Integer> getIdCategoriesMeres() {
	return idCategoriesMeres;
    }

    /**
     * @param idCategoriesMeres the idCategoriesMeres to set
     */
    public void setIdCategoriesMeres(ArrayList<Integer> idCategoriesMeres) {
	this.idCategoriesMeres = idCategoriesMeres;
    }

    /**
     * @return the identifiant
     */
    public Integer getIdentifiant() {
	return identifiant;
    }

    /**
     * @param identifiant the identifiant to set
     */
    public void setIdentifiant(Integer identifiant) {
	this.identifiant = identifiant;
    }

    /**
     * @return the urlCategory
     */
    public String getUrlCategory() {
	return urlCategory;
    }

    /**
     * @param urlCategory the urlCategory to set
     */
    public void setUrlCategory(String urlCategory) {
	this.urlCategory = urlCategory;
    }

    /**
     * @return the nbImages
     */
    public Integer getNbImages() {
	return nbImages;
    }

    /**
     * @param nbImages the nbImages to set
     */
    public void setNbImages(Integer nbImages) {
	this.nbImages = nbImages;
    }

    /**
     * @return the nbTotalImages
     */
    public Integer getNbTotalImages() {
	return nbTotalImages;
    }

    /**
     * @param nbTotalImages the nbTotalImages to set
     */
    public void setNbTotalImages(Integer nbTotalImages) {
	this.nbTotalImages = nbTotalImages;
    }

    /**
     * @return the categoriesFilles
     */
    public ArrayList<Category> getCategoriesFilles() {
	return categoriesFilles;
    }

    /**
     * @param categoriesFilles the categoriesFilles to set
     */
    public void setCategoriesFilles(ArrayList<Category> categoriesFilles) {
	this.categoriesFilles = categoriesFilles;
    }

    /**
     * @return the categoriesMeres
     */
    public ArrayList<Category> getCategoriesMeres() {
	return categoriesMeres;
    }

    /**
     * @param categoriesMeres the categoriesMeres to set
     */
    public void setCategoriesMeres(ArrayList<Category> categoriesMeres) {
	this.categoriesMeres = categoriesMeres;
    }

    /**
     * @return the nom
     */
    public String getNom() {
	return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
	this.nom = nom;
    }

    /**
     * @return the element
     */
    public Element getElement() {
	return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(Element element) {
	this.element = element;
    }

    /**
     * @return the parentDirect
     */
    public Integer getParentDirect() {
	return parentDirect;
    }

    /**
     * @param parentDirect the parentDirect to set
     */
    public void setParentDirect(Integer parentDirect) {
	this.parentDirect = parentDirect;
    }

}

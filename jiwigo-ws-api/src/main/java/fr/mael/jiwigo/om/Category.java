package fr.mael.jiwigo.om;

import java.util.ArrayList;

import org.w3c.dom.Element;

/*
DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
        Version 2, December 2004

Copyright (C) 2011 Mael <mael@le-guevel.com>

Everyone is permitted to copy and distribute verbatim or modified
copies of this license document, and changing it is allowed as long
as the name is changed.

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

0. You just DO WHAT THE FUCK YOU WANT TO.

*/
/**

 * @author mael
 *
 */
public class Category {

    private ArrayList<Integer> idParentCategories;
    private String idParentCategoriesString;
    private Integer identifier;
    private String urlCategory;
    private Integer nbImages;
    private Integer nbTotalImages;
    private ArrayList<Category> childCategories;
    private ArrayList<Category> parentCategories;
    private String name;
    private Element element;
    private Integer directParent;
    private Integer level;
    private String urlThumbnail;

    public Category() {
	childCategories = new ArrayList<Category>();
	parentCategories = new ArrayList<Category>();
    }

    @Override
    public String toString() {
	return name;
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

    public Integer getLevel() {
	return level;
    }

    public void setLevel(Integer level) {
	this.level = level;
    }

    public ArrayList<Integer> getIdParentCategories() {
	return idParentCategories;
    }

    public void setIdParentCategories(ArrayList<Integer> idParentCategories) {
	this.idParentCategories = idParentCategories;
    }

    public Integer getIdentifier() {
	return identifier;
    }

    public void setIdentifier(Integer identifier) {
	this.identifier = identifier;
    }

    public ArrayList<Category> getChildCategories() {
	return childCategories;
    }

    public void setChildCategories(ArrayList<Category> childCategories) {
	this.childCategories = childCategories;
    }

    public ArrayList<Category> getParentCategories() {
	return parentCategories;
    }

    public void setParentCategories(ArrayList<Category> parentCategories) {
	this.parentCategories = parentCategories;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getDirectParent() {
	return directParent;
    }

    public void setDirectParent(Integer directParent) {
	this.directParent = directParent;
    }

    public String getIdParentCategoriesString() {
	return idParentCategoriesString;
    }

    public void setIdParentCategoriesString(String idParentCategoriesString) {
	this.idParentCategoriesString = idParentCategoriesString;
    }
    /**
     * @return the urlThumbnail
     */
    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    /**
     * @param urlThumbnail the urlThumbnail to set
     */
    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

}

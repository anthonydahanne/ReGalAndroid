package fr.mael.jiwigo.om;

import java.io.File;

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
 *

 * @author mael
 *
 */
public class Image {
    private String thumbnailUrl;
    private String url;
    private Integer width;
    private Integer height;
    private String file;
    private Integer identifier;
    private Integer seen;
    private String name;
    private Integer idCategory;
    private String author;
    private File thumbnail;
    private String privacyLevel;
    private File originale;
    private String resizedUrl;

    /**
     * @return the privacyLevel
     */
    public String getPrivacyLevel() {
	return privacyLevel;
    }

    /**
     * @param privacyLevel the privacyLevel to set
     */
    public void setPrivacyLevel(String privacyLevel) {
	this.privacyLevel = privacyLevel;
    }

    public Integer getSeen() {
	return seen;
    }

    public void setSeen(Integer seen) {
	this.seen = seen;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    /**
     * @return the thumbnail
     */
    public File getThumbnail() {
	return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(File thumbnail) {
	this.thumbnail = thumbnail;
    }

    /**
     * @return the originale
     */
    public File getOriginale() {
	return originale;
    }

    /**
     * @param originale the originale to set
     */
    public void setOriginale(File originale) {
	this.originale = originale;
    }

    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return name;
    }

    public String getThumbnailUrl() {
	return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
	this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
	return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Integer width) {
	this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
	return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(Integer height) {
	this.height = height;
    }

    /**
     * @return the file
     */
    public String getFile() {
	return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
	this.file = file;
    }

    public Integer getIdentifier() {
	return identifier;
    }

    public void setIdentifier(Integer identifier) {
	this.identifier = identifier;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the idCategory
     */
    public Integer getIdCategory() {
	return idCategory;
    }

    /**
     * @param idCategory the idCategory to set
     */
    public void setIdCategory(Integer idCategory) {
	this.idCategory = idCategory;
    }

    /**
     * @return the resized url
     */
    public String getResizedUrl() {
        return resizedUrl;
    }

    /**
     * @param resizedUrl the resized url to set
     */
    public void setResizedUrl(String resizedUrl) {
        this.resizedUrl = resizedUrl;
    }

}

package fr.mael.jiwigo.om;

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
public class Comment {
    private Integer imageId;
    private String author;
    private String content;
    private String key;
    private Integer identifier;
    private String date;

    /**
     * @return the imageId
     */
    public Integer getImageId() {
	return imageId;
    }

    /**
     * @param imageId the imageId to set
     */
    public void setImageId(Integer imageId) {
	this.imageId = imageId;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
	return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
	this.author = author;
    }

    /**
     * @return the content
     */
    public String getContent() {
	return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
	this.content = content;
    }

    /**
     * @return the key
     */
    public String getKey() {
	return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
	this.key = key;
    }

    public Integer getIdentifier() {
	return identifier;
    }

    public void setIdentifier(Integer identifier) {
	this.identifier = identifier;
    }

    /**
     * @return the date
     */
    public String getDate() {
	return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
	this.date = date;
    }

}

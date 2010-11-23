package fr.mael.jiwigo.om;

import java.io.File;

/**
 * 
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
public class Image {
    private String miniature;
    private String url;
    private Integer width;
    private Integer height;
    private String file;
    private Integer identifiant;
    private Integer vue;
    private String name;
    private Integer idCategory;
    private String auteur;
    private File thumbnail;

    /**
     * @return the auteur
     */
    public String getAuteur() {
	return auteur;
    }

    /**
     * @param auteur the auteur to set
     */
    public void setAuteur(String auteur) {
	this.auteur = auteur;
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

    private File originale;

    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return name;
    }

    /**
     * @return the miniature
     */
    public String getMiniature() {
	return miniature;
    }

    /**
     * @param miniature the miniature to set
     */
    public void setMiniature(String miniature) {
	this.miniature = miniature;
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
     * @return the vue
     */
    public Integer getVue() {
	return vue;
    }

    /**
     * @param vue the vue to set
     */
    public void setVue(Integer vue) {
	this.vue = vue;
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

}

package fr.mael.jiwigo.om;

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
public class Comment {
    private Integer imageId;
    private String author;
    private String content;
    private String key;
    private Integer identifiant;
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

/**
 * 
 */
package fr.mael.jiwigo.transverse.enumeration;

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
   
 * Lists the methods of the webservice
 * @author mael
 *
 */
public enum MethodsEnum {

    LOGIN("pwg.session.login"), LISTER_CATEGORIES("pwg.categories.getList"), LISTER_IMAGES("pwg.categories.getImages"), GET_INFO(
	    "pwg.images.getInfo"), AJOUTER_CATEGORIE("pwg.categories.add"), AJOUTER_COMMENTAIRE("pwg.images.addComment"), LISTER_TAGS(
	    "pwg.tags.getList"), TAGS_ADMIN_LIST("pwg.tags.getAdminList"), ADD_TAG("pwg.tags.add"), SET_INFO(
	    "pwg.images.setInfo"), SEARCH("pwg.images.search"), SET_PRIVACY_LEVEL("pwg.images.setPrivacyLevel");

    protected String label;

    /** Constructeur */
    MethodsEnum(String pLabel) {
	this.label = pLabel;
    }

    public String getLabel() {
	return this.label;
    }
}

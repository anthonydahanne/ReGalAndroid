/**
 *
 */
package fr.mael.jiwigo.transverse.enumeration;

/*
 *  jiwigo-ws-api Piwigo webservice access Api
 *  Copyright (c) 2010-2011 Mael mael@le-guevel.com
 *                All Rights Reserved
 *
 *  This library is free software. It comes without any warranty, to
 *  the extent permitted by applicable law. You can redistribute it
 *  and/or modify it under the terms of the Do What The Fuck You Want
 *  To Public License, Version 2, as published by Sam Hocevar. See
 *  http://sam.zoy.org/wtfpl/COPYING for more details.
 */
/**

 * Lists the methods of the webservice
 * @author mael
 *
 */
public enum MethodsEnum {

    LOGIN("pwg.session.login"), LISTER_CATEGORIES("pwg.categories.getList"), LISTER_IMAGES("pwg.categories.getImages"), GET_INFO(
	    "pwg.images.getInfo"), AJOUTER_CATEGORIE("pwg.categories.add"), AJOUTER_COMMENTAIRE("pwg.images.addComment"), LISTER_TAGS(
	    "pwg.tags.getList"), TAGS_ADMIN_LIST("pwg.tags.getAdminList"), ADD_TAG("pwg.tags.add"), SET_INFO(
	    "pwg.images.setInfo"), SEARCH("pwg.images.search"), SET_PRIVACY_LEVEL("pwg.images.setPrivacyLevel"), ADD_SIMPLE(
	    "pwg.images.addSimple"), ADD_IMAGE("pwg.images.add"), ADD_CHUNK("pwg.images.addChunk"), DELETE_CATEGORY(
	    "pwg.categories.delete"), DELETE_IMAGE("pwg.images.delete"), SESSION_STATUS("pwg.session.getStatus"), IMAGE_EXIST(
	    "pwg.images.exist");

    protected String label;

    /** Constructeur */
    MethodsEnum(String pLabel) {
	this.label = pLabel;
    }

    public String getLabel() {
	return this.label;
    }
}

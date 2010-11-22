package net.dahanne.gallery.commons.utils;

import net.dahanne.gallery.commons.remote.GalleryConnectionException;

public class SerializationException extends Exception {

	private static final long serialVersionUID = 4185267365609877943L;
	private final static String message = GalleryConnectionException.class
			.getName();

	/**
	 * @param msg
	 */
	public SerializationException(String msg) {
		super(message + " : " + msg);
	}

	public SerializationException(Throwable t) {
		super(t);
	}
}

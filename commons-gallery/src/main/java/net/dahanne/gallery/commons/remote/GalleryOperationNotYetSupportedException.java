package net.dahanne.gallery.commons.remote;

public class GalleryOperationNotYetSupportedException extends GalleryConnectionException {


	private static final long serialVersionUID = 8468892237712525290L;

	public GalleryOperationNotYetSupportedException(String msg) {
		super(msg);
	}

	public GalleryOperationNotYetSupportedException(Throwable t) {
		super(t);
	}

}

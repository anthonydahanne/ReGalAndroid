package net.dahanne.android.regalandroid.utils;

public class ImpossibleToLoginException extends GalleryConnectionException {

	private static final long serialVersionUID = -8277215227949719569L;

	public ImpossibleToLoginException(String msg) {
		super(msg);
	}

	public ImpossibleToLoginException(Throwable t) {
		super(t);
	}

}

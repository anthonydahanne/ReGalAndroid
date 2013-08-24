package fr.mael.jiwigo.transverse.exception;

public class JiwigoException extends Exception {

    private static final long serialVersionUID = -6224666480360206953L;

    public JiwigoException(String msg) {
	super(msg);
    }

    public JiwigoException(Throwable t) {
	super(t);
    }

    public JiwigoException(String msg, Throwable t) {
	super(msg, t);
    }

}
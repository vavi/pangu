package net.pangu.common.exception;

public class PanguRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -6646534085839252362L;

    public PanguRuntimeException(Exception e) {
	super(e);
    }

}

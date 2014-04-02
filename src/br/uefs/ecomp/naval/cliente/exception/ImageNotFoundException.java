package br.uefs.ecomp.naval.cliente.exception;



public class ImageNotFoundException extends Exception {

	public ImageNotFoundException() {
	}

	public ImageNotFoundException(String message) {
		super(message);
	}

	public ImageNotFoundException(Throwable cause) {
		super(cause);
	}

	public ImageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

package com.learning.api.v1.stocks.exceptions;

public class StockResponseException extends Exception{

	private static final long serialVersionUID = 1L;
	public StockResponseException(String message) {
        super(message);
    }

}

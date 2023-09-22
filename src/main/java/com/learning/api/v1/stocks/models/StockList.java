package com.learning.api.v1.stocks.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockList {

	    List<Stock> stockList;

	    public StockList(List<Stock> stockList) {
	        this.stockList = stockList;
	    }
	    
}
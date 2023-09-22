package com.learning.api.v1.stocks.services;

import java.util.List;

import com.learning.api.v1.stocks.exceptions.StockResponseException;
import com.learning.api.v1.stocks.models.Stock;
import com.learning.api.v1.stocks.models.StockGeneralResponse;

public interface StockService {

    void populateStockDatabase() throws StockResponseException;
    StockGeneralResponse<List<Stock>> getAllStocks();
    StockGeneralResponse<Stock> getStockBySymbol(String symbol);
    StockGeneralResponse<Stock> createStock(Stock stock);
    StockGeneralResponse<Stock> updateStock(String symbol, Double lastPrice) throws StockResponseException; // throws exception
    StockGeneralResponse<String> deleteStock(String symbol);
}
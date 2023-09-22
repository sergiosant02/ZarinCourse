package com.learning.api.v1.stocks.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.learning.api.v1.stocks.models.Stock;

@Repository	
public interface StockRepository extends MongoRepository<Stock, String>{
	Stock findBySymbol(String symbol);
	void deleteDistintBySymbol(String symbol);
}

package com.learning.api.v1.stocks.services;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.learning.api.v1.stocks.exceptions.StockResponseException;
import com.learning.api.v1.stocks.models.*;
import com.learning.api.v1.stocks.repositories.StockRepository;

import java.util.List;
import java.util.Objects;

@Service 
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final RestTemplate stocksApiRestTemplate;

    private final HttpHeaders httpHeaders;

    public StockServiceImpl(
      // the @Qualifier annotation will ensure that the correct REST template is being instantiated in the constructor 
      @Qualifier("stocksApiRestTemplate") RestTemplate stocksApiRestTemplate,
                            StockRepository stockRepository) {
        this.stocksApiRestTemplate = stocksApiRestTemplate;
        this.stockRepository = stockRepository;

        // instantiating headers and passing in the API key to the header name "x-rapidapi-key", which can be found in the API documentation
        httpHeaders = new HttpHeaders();
        httpHeaders.add("x-rapidapi-key", "9e87a2c143msh6b92309e36af212p15ccc6jsn2bc37ea481bd");
    }

    //---------------------------------------------------------------------------------------------------------------

    @Scheduled(fixedRate = 60000)
    public void populateStockDatabase() throws StockResponseException {
        String path = "/price";

      
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(path)
                .queryParam("Indices", "NIFTY NEXT 50");

        // a FastList is an attempt to provide the same functionality as ArrayList without the support for concurrent modification exceptions
        // we set the type of data that will be collected in this FastList, i.e. Stock 
        // we use the rest template and apply the "exchange" function to it 
        // the exchange method takes in the builder object which will result in --> /price?Indices=NIFTY%20NEXT%2050
        // we pass in the CRUD operation as GET and pass in the headers that were previously instantiated
        ResponseEntity<FastList<Stock>> response = stocksApiRestTemplate.exchange(builder.build().toString(), HttpMethod.GET, new HttpEntity<>(httpHeaders), new ParameterizedTypeReference<FastList<Stock>>() {
        });

        // if the response status is a 200 or OK...
        if (response.getStatusCode() == HttpStatus.OK) {
            // we ensure that the response is not null, and then save the body of the response object into our database via the stock respository's default given method, save()
            Objects.requireNonNull(response.getBody()).forEach(stockRepository::save);
        } else {
            // if an issue occurs, we throw an exception with a specific error message, in String format
            throw new StockResponseException("Error: Issue retrieving stocks.");
        }
    }

	@Override
	public StockGeneralResponse<List<Stock>> getAllStocks() {
		return new StockGeneralResponse<List<Stock>>(stockRepository.findAll(), HttpStatus.OK);
	}

	@Override
	public StockGeneralResponse getStockBySymbol(String symbol) {
	    // the method should automatically capitalize the symbol so that there are no issues regarding case-sensitive querying
	    Stock stock = stockRepository.findBySymbol(symbol.toUpperCase());
	    StockGeneralResponse<Stock> res;
	    if (stock != null) {
	        res = new StockGeneralResponse<Stock>(stock, HttpStatus.OK);
	    } else {
	        res = new StockGeneralResponse(symbol, HttpStatus.NOT_FOUND);
	    }
	    return res;
	}

	@Override
	public StockGeneralResponse<Stock> createStock(Stock stock) {
		stockRepository.save(stock);
		return new StockGeneralResponse<Stock>(stock, HttpStatus.CREATED);
	}


	@Override
	public StockGeneralResponse<Stock> updateStock(String symbol, Double lastPrice) throws StockResponseException {
		Stock stock = stockRepository.findBySymbol(symbol.toUpperCase());
		if(stock != null) {
			stock.setLastPrice(lastPrice);
			stockRepository.save(stock);
		} else {
			throw new StockResponseException("Not foud stock");
		}
		return new StockGeneralResponse<Stock>(stock, HttpStatus.ACCEPTED);
	}

	@Override
	public StockGeneralResponse<String> deleteStock(String symbol) {
		stockRepository.deleteDistintBySymbol(symbol);
		return new StockGeneralResponse<String>(symbol, HttpStatus.NO_CONTENT);
	}
}
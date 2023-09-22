package com.learning.api.v1.stocks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.learning.api.v1.stocks.exceptions.StockResponseException;
import com.learning.api.v1.stocks.models.*;
import com.learning.api.v1.stocks.services.StockService;

// include the following media type constants 
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

@RestController
@RequestMapping(
        value="/api/v1",
        consumes = {APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE, ALL_VALUE},
        produces = {APPLICATION_JSON_VALUE})
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful: Stock(s) found."),
        @ApiResponse(responseCode = "201", description = "Created: Stock created."),
        @ApiResponse(responseCode = "202", description = "Accepted: Stock updated."),
        @ApiResponse(responseCode = "204", description = "No Content: Stock deleted."),
        @ApiResponse(responseCode = "400", description = "Bad Request: Check input parameter(s) syntax for invalid characters."),
        @ApiResponse(responseCode = "401", description = "Unauthorized: User is not entitled to retrieve information."),
        @ApiResponse(responseCode = "404", description = "Not Found: Stock(s) not found."),
        @ApiResponse(responseCode = "500", description = "Internal Server Error: Backend service is down.")
})
public class StockController {

    
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    //---------------------------------------------------------------------------------------------------------------

    // annotatation verbalizes what the endpoint is used for 
    @Operation(summary = "Get all stocks")
    @GetMapping(value = "/stocks")
    public ResponseEntity<StockGeneralResponse<List<Stock>>> getAllStocks() {

        ResponseEntity<StockGeneralResponse<List<Stock>>> responseEntity;
        // call the getAllStocks method from the stock service
        StockGeneralResponse serviceResponse = stockService.getAllStocks();
        responseEntity = new ResponseEntity<>(serviceResponse, serviceResponse.getHttpStatus());
        return responseEntity;
    }
    
    @Operation(summary = "Get a stock by symbol")
    @GetMapping(value = "/stocks/{symbol}")
	public ResponseEntity<StockGeneralResponse> getStockBySymbol(
	         @Parameter(description = "A stock symbol", required = true)
	         @PathVariable() String symbol) {
	
	     ResponseEntity<StockGeneralResponse> responseEntity;
	     // call the service class, passing in the input variable to the function
	     StockGeneralResponse serviceResponse = stockService.getStockBySymbol(symbol);
	     responseEntity = new ResponseEntity<>(serviceResponse, serviceResponse.getHttpStatus());
	     return responseEntity;
	 }
    
    @Operation(summary = "Create a Stock")
    @PostMapping(value="/stocks")
    public ResponseEntity<StockGeneralResponse> createPost(@Parameter(description = "The new Stock object", required = true) @RequestBody() Stock stock){
    	StockGeneralResponse serviceRespond = stockService.createStock(stock);
    	return new ResponseEntity<StockGeneralResponse>(serviceRespond, serviceRespond.getHttpStatus());
    }
    
    @Operation(summary = "Update a Stock")
    @PutMapping(value="/stocks/{symbol}")
    public ResponseEntity<StockGeneralResponse> updatePost(@Parameter(description = "A stock symbol", required = true) @PathVariable(name = "symbol") String symbol, @Parameter(description = "The update Stock price", required = true) @RequestParam() Double newPrice) throws StockResponseException{
    	StockGeneralResponse serviceRespond = stockService.updateStock(symbol,newPrice);
    	return new ResponseEntity<StockGeneralResponse>(serviceRespond, serviceRespond.getHttpStatus());
    }

}
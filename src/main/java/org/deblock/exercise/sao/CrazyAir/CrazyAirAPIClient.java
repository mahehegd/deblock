package org.deblock.exercise.sao.CrazyAir;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.model.Suppliers;
import org.deblock.exercise.sao.APIClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CrazyAirAPIClient implements APIClient<CrazyAirRequestObject, CrazyAirResponseObject>{
private final String url = "http://localhost:8001/v1/flights"; //TODO move to application.properties and fetch using @Value
  
    @Override
    public List<SearchResponseParam> fetchFlights(CrazyAirRequestObject request) {

        RestTemplate restTemplate = new RestTemplate(); //TODO create bean in @Configuration to @Autowired
        RequestEntity<CrazyAirRequestObject> requestEntity = 
            new RequestEntity<CrazyAirRequestObject>(request, HttpMethod.GET, URI.create(url));
    
        ResponseEntity<List<CrazyAirResponseObject>> response = 
            restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<CrazyAirResponseObject>>() {});
        
            List<CrazyAirResponseObject> data = response.getBody();
            return data.stream().map(d -> getSearchResponseFromResponseObject(d)).collect(Collectors.toList());
    }

    @Override
    public SearchResponseParam getSearchResponseFromResponseObject(CrazyAirResponseObject response) {
        return new SearchResponseParam(response.getAirline(), 
        Suppliers.crazyAir,
        computeFare(response.getPrice(),Double.NaN, Double.NaN),
        response.getDepartureAirportCode(),
        response.getDestinationAirportCode(),
        response.getDepartureDate(), response.getArrivalDate());
    }       

    @Override
      public CrazyAirRequestObject getRequestObjectFromSearchParam(SearchRequestParam request){
        return new CrazyAirRequestObject(request.getOrigin(),
        request.getDestination(), request.getDepartureDate(), request.getReturnDate(), request.getNumberOfPassengers()            
        );   
    }

    @Override
    public Double computeFare(Double base, Double tax, Double discount) {
        return base;
    }

    @Override
    public Suppliers getSupplier() {
        return Suppliers.crazyAir;
    }
    
}

package org.deblock.exercise.sao.ToughJet;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.model.Suppliers;
import org.deblock.exercise.sao.APIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ToughJetAPIClient implements APIClient<ToughJetRequestObject, ToughJetResponseObject> {
    @Value("${tough_jet.url}")
    private String url ;
    //= "http://localhost:8002/v1/getFlights";

    @Override
    public List<SearchResponseParam> fetchFlights(ToughJetRequestObject request) {
        log.info(String.format("url = %s", url));
        try {
            RestTemplate restTemplate = new RestTemplate(); // TODO create bean in @Configuration to @Autowired
            RequestEntity<ToughJetRequestObject> requestEntity = new RequestEntity<ToughJetRequestObject>(request,
                    HttpMethod.GET, URI.create(url));

            ResponseEntity<List<ToughJetResponseObject>> response = restTemplate.exchange(requestEntity,
                    new ParameterizedTypeReference<List<ToughJetResponseObject>>() {
                    });

            List<ToughJetResponseObject> data = response.getBody();
            return data.stream().map(d -> getSearchResponseFromResponseObject(d)).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ToughJetRequestObject getRequestObjectFromSearchParam(SearchRequestParam request) {
        return new ToughJetRequestObject(request.getOrigin(),
                request.getDestination(),
                request.getDepartureDate(),
                request.getReturnDate(),
                request.getNumberOfPassengers());
    }

    @Override
    public SearchResponseParam getSearchResponseFromResponseObject(ToughJetResponseObject response) {
        return new SearchResponseParam(
                response.getCarrier(),
                getSupplier(),
                computeFare(response.getBasePrice(), response.getTax(), response.getDiscount()),
                response.getDepartureAirportName(),
                response.getArrivalAirportName(),
                response.getOutboundDateTime(),
                response.getInboundDateTime());
    }

    @Override
    public Double computeFare(Double base, Double tax, Double discount) {
        Double priceAfterTax = base + ((base * tax) / 100);
        Double discountAmount = ((base * discount) / 100);
        return priceAfterTax - discountAmount;

    }

    @Override
    public Suppliers getSupplier() {
        return Suppliers.toughJet;
    }
}

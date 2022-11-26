package org.deblock.exercise.sao;

import java.util.List;
import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.model.Suppliers;

public interface APIClient<T1,T2> {
    List<SearchResponseParam> fetchFlights(T1 request);
    T1 getRequestObjectFromSearchParam(SearchRequestParam request);
    SearchResponseParam getSearchResponseFromResponseObject(T2 response);
    Double computeFare(Double base, Double tax, Double discount);
    Suppliers getSupplier();
}

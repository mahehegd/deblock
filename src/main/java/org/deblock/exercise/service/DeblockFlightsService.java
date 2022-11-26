package org.deblock.exercise.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.sao.APIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeblockFlightsService extends Throwable{
    @Autowired
    List<APIClient> clients;
    
    public List<SearchResponseParam> fetchFlights(SearchRequestParam request){
        System.out.println(clients.size());
        List<CompletableFuture<List<SearchResponseParam>>> futures = clients.stream()
            .map(client -> CompletableFuture.supplyAsync(() -> {
                List<SearchResponseParam> responseParams =  client.fetchFlights(client.getRequestObjectFromSearchParam(request));
                return responseParams;
            }))
            .collect(Collectors.toList());
    try {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
        // TODO log the api call that is taking longer than 60 seconds.
    }
    List<SearchResponseParam> results = futures
            .stream()
            .filter(future -> future.isDone() && !future.isCompletedExceptionally())
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .sorted((f1,f2) ->f1.getFare().compareTo(f2.getFare()))
            .collect(Collectors.toList()); 

    return results;
    }
}

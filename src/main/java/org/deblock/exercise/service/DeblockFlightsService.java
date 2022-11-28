package org.deblock.exercise.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.deblock.exercise.exceptions.InternalServiceFailureException;
import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.sao.APIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeblockFlightsService extends Throwable {
    @Autowired
    List<APIClient> clients;

    public List<SearchResponseParam> getFlights(SearchRequestParam request) {
        log.info(String.format("Client size = %d", clients.size()));
        try {
            List<CompletableFuture<List<SearchResponseParam>>> futures = clients.stream()
                    .map(client -> CompletableFuture.supplyAsync(() -> {
                        List<SearchResponseParam> responseParams = client
                                .fetchFlights(client.getRequestObjectFromSearchParam(request));
                        return responseParams;
                    }))
                    .collect(Collectors.toList());
            //Consider all the requests completed within 1 minute
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);

            List<SearchResponseParam> results = futures
                    .stream()
                    .filter(future -> future.isDone() && !future.isCompletedExceptionally())
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .sorted((f1, f2) -> f1.getFare().compareTo(f2.getFare()))
                    .collect(Collectors.toList());
            // TODO sorting and paging by slicing the api response.
            return results;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            //TODO for InterruptedException | ExecutionException | TimeoutException log for requests taking more than 1 minute
            throw new InternalServiceFailureException(ex.getMessage());
        }
    }
}

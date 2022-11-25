
public class DeblockFlightController {
    @RequestMapping("/")
    public String home() {
        return "Hi deblockFlights!";
    }

    @PostMapping(value = "/v1/search")
    public ResponseEntity<FlightSearchResult> getFlights(
            @Valid @RequestBody FlightSearchRequestParameters searchParameters,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "fare") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        List<FlightDetail> flightDetails = flightAggregationService.getFlights(searchParameters, pageable);
        FlightSearchResult flightSearchResult = new FlightSearchResult();
        flightSearchResult.setResponses(flightDetails);

        return new ResponseEntity<>(flightSearchResult, HttpStatus.OK);
    }
}
package com.deathmetalmania.service;

import com.deathmetalmania.model.api.ticketmaster.TicketmasterApi;
import com.deathmetalmania.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RestTicketmasterService implements TicketmasterService {

    private final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private final String ticketmasterApiKey;


    public RestTicketmasterService(@Value("${TICKETMASTER_API_KEY}") String apiKey){
        this.ticketmasterApiKey = apiKey;
    }

    public TicketmasterApi searchEventsByBand(String bandName) {
        // Construct URL for the Ticketmaster API request
        String url = BASE_URL + "?keyword=" + bandName + "&apikey=" + ticketmasterApiKey;

        try {

            RestClient restClient = RestClient.create();
            TicketmasterApi fullResults = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(TicketmasterApi.class);

            if (fullResults == null) {
                throw new ServiceException("No events found for the band: " + bandName);
            }


            return fullResults;

        } catch (Exception e) {
            throw new ServiceException("Error searching for events by band: " + bandName, e);
        }
    }
}
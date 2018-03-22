package com.jdum.booking.webface.client;

import com.jdum.booking.common.dto.SearchQuery;
import com.jdum.booking.common.dto.TripDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author idumchykov
 * @since 1/24/18
 */
@FeignClient(name = "${client.search.service}")
public interface SearchClient {

    @RequestMapping(value = "${client.search.requests.get}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<TripDTO> getTrips(SearchQuery searchQuery);
}

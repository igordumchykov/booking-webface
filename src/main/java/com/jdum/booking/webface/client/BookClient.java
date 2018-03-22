package com.jdum.booking.webface.client;

import com.jdum.booking.common.dto.BookingRecordDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author idumchykov
 * @since 1/24/18
 */
@FeignClient(name = "${client.booking.service}")
public interface BookClient {

    @RequestMapping(method = RequestMethod.POST, value = "${client.booking.requests.create}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Long create(BookingRecordDTO bookingRecord);

    @RequestMapping(method = RequestMethod.GET, value = "${client.booking.requests.get}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BookingRecordDTO getBookingRecord(@PathVariable("id") Long id);
}

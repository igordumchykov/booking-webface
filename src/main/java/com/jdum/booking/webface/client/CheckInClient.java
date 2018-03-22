package com.jdum.booking.webface.client;

import com.jdum.booking.common.dto.CheckInRecordDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author idumchykov
 * @since 1/24/18
 */
@FeignClient(name = "${client.checkin.service}")
public interface CheckInClient {

    @RequestMapping(method = RequestMethod.POST, value = "${client.checkin.requests.create}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Long create(CheckInRecordDTO checkInRecord);
}

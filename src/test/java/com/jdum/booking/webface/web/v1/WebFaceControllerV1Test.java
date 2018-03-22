package com.jdum.booking.webface.web.v1;

import com.jdum.booking.common.dto.*;
import com.jdum.booking.common.exceptions.NotFoundException;
import com.jdum.booking.webface.client.BookClient;
import com.jdum.booking.webface.client.CheckInClient;
import com.jdum.booking.webface.client.SearchClient;
import com.jdum.booking.webface.dto.UIData;
import com.jdum.booking.webface.exceptions.ExceptionHandlerControllerAdvice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.jdum.booking.webface.constants.Constants.*;
import static com.jdum.booking.webface.constants.REST.*;
import static com.jdum.booking.webface.util.TestDataCreator.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author idumchykov
 * @since 2/16/18
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {WebFaceControllerV1.class, ExceptionHandlerControllerAdvice.class})
public class WebFaceControllerV1Test {

    @MockBean
    private SearchClient searchClient;

    @MockBean
    private BookClient bookClient;

    @MockBean
    private CheckInClient checkInClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnCorrectView() throws Exception {
        mockMvc.perform(get(API_V1 + INDEX_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(UIDATA_ATTRIBUTE, new UIData(SearchQuery.getDefault())))
                .andExpect(view().name(SEARCH_VIEW));
    }

    @Test
    public void shouldReturnTrips() throws Exception {
        List<TripDTO> trips = constructTrips();

        when(searchClient.getTrips(any(SearchQuery.class))).thenReturn(trips);

        mockMvc.perform(post(API_V1 + TRIP_SEARCH_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr(UIDATA_ATTRIBUTE, new UIData(SearchQuery.getDefault())))
                .andExpect(status().isOk())
                .andExpect(view().name(SEARCH_TRIP_RESULT_VIEW))
                .andExpect(model().attribute(UIDATA_ATTRIBUTE,
                        new UIData(SearchQuery.getDefault())
                                .setTrips(trips)));
    }

    @Test
    public void shouldReturn404IfTripsNotFound() throws Exception {

        doThrow(NotFoundException.class).when(searchClient).getTrips(any(SearchQuery.class));

        mockMvc.perform(post(API_V1 + TRIP_SEARCH_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr(UIDATA_ATTRIBUTE, new UIData(SearchQuery.getDefault())))
                .andExpect(status().isNotFound())
                .andExpect(view().name(NOT_FOUND_VIEW_NAME));
    }

    @Test
    public void shouldReturnBookingView() throws Exception {
        TripDTO trip = constructTrip();

        mockMvc.perform(post(API_V1 + BOOKING_BOOK_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr(TRIP_ATTRIBUTE, trip))
                .andExpect(status().isOk())
                .andExpect(view().name(BOOKING_INPUT_VIEW))
                .andExpect(model().attribute(UIDATA_ATTRIBUTE, new UIData(trip, new PassengerDTO())));
    }

    @Test
    public void shouldCreateBookingWhenConfirm() throws Exception {
        TripDTO trip = constructTrip();

        when(bookClient.create(any(BookingRecordDTO.class))).thenReturn(BOOK_ID);

        mockMvc.perform(post(API_V1 + BOOKING_CONFIRM_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr(UIDATA_ATTRIBUTE, new UIData(trip, new PassengerDTO())))
                .andExpect(status().isOk())
                .andExpect(view().name(BOOKING_CONFIRMATION_VIEW))
                .andExpect(model().attribute(MESSAGE_ATTRIBUTE, BOOKING_CONFIRMED_MSG + BOOK_ID));
    }

    @Test
    public void shouldReturnSearchBookingView() throws Exception {
        mockMvc.perform(get(API_V1 + BOOKING_GET_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(BOOKING_DETAILS_VIEW));
    }

    @Test
    public void shouldGetBookingDetails() throws Exception {
        BookingRecordDTO bookingRecordDTO = constructBookingDTO();

        when(bookClient.getBookingRecord(BOOK_ID)).thenReturn(bookingRecordDTO);

        mockMvc.perform(post(API_V1 + BOOKING_GET_DETAILS_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr(UIDATA_ATTRIBUTE, new UIData()
                        .setBookingId(BOOK_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name(BOOKING_DETAILS_VIEW))
                .andExpect(model().attribute(UIDATA_ATTRIBUTE,
                        new UIData()
                                .setPassenger(constructPassenger(bookingRecordDTO))
                                .setSelectedTrip(constructTrip(bookingRecordDTO))
                                .setBookingId(BOOK_ID)));
    }

    @Test
    public void bookQuery() throws Exception {
        CheckInRecordDTO checkInRecord = constructCheckInRecord();

        when(checkInClient.create(any())).thenReturn(CHECK_IN_ID);

        mockMvc.perform(post(API_V1 + BOOKING_CHECK_IN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .flashAttr(CHECK_IN_ATTRIBUTE, checkInRecord))
                .andExpect(status().isOk())
                .andExpect(view().name(CHECK_IN_CONFIRM_VIEW))
                .andExpect(model().attribute(MESSAGE_ATTRIBUTE, BOOKING_CHECK_IN_MSG + CHECK_IN_ID));
    }
}
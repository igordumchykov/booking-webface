package com.jdum.booking.webface.web.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdum.booking.common.dto.*;
import com.jdum.booking.common.exceptions.NotFoundException;
import com.jdum.booking.webface.client.BookClient;
import com.jdum.booking.webface.client.CheckInClient;
import com.jdum.booking.webface.client.SearchClient;
import com.jdum.booking.webface.dto.UIData;
import com.jdum.booking.webface.exceptions.ExceptionHandlerControllerAdvice;
import org.junit.Before;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author idumchykov
 * @since 2/16/18
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {WebFaceControllerV2.class, ExceptionHandlerControllerAdvice.class})
public class WebFaceControllerV2Test {

    @MockBean
    private SearchClient searchClient;

    @MockBean
    private BookClient bookClient;

    @MockBean
    private CheckInClient checkInClient;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private String SEARCH_JSON_REQUEST;

    @Before
    public void setUp() throws Exception {
        SEARCH_JSON_REQUEST = mapper.writeValueAsString(new UIData(SearchQuery.getDefault()));
    }

    @Test
    public void shouldReturnCorrectSearchQuery() throws Exception {
        mockMvc.perform(get(API_V2 + INDEX_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string(SEARCH_JSON_REQUEST));
    }

    @Test
    public void shouldReturnTrips() throws Exception {
        List<TripDTO> trips = constructTrips();

        when(searchClient.getTrips(any(SearchQuery.class))).thenReturn(trips);

        mockMvc.perform(post(API_V2 + TRIP_SEARCH_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(SEARCH_JSON_REQUEST))
                .andExpect(content().string(mapper.writeValueAsString(
                        new UIData(SearchQuery.getDefault()).setTrips(trips))));
    }

    @Test
    public void shouldReturn404IfTripsNotFound() throws Exception {

        doThrow(NotFoundException.class).when(searchClient).getTrips(any(SearchQuery.class));

        mockMvc.perform(post(API_V2 + TRIP_SEARCH_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(SEARCH_JSON_REQUEST))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBookingView() throws Exception {
        TripDTO trip = constructTrip();

        mockMvc.perform(post(API_V2 + BOOKING_BOOK_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsString(trip)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new UIData(trip, new PassengerDTO()))));
    }

    @Test
    public void shouldCreateBookingWhenConfirm() throws Exception {
        TripDTO trip = constructTrip();

        when(bookClient.create(any(BookingRecordDTO.class))).thenReturn(BOOK_ID);

        mockMvc.perform(post(API_V2 + BOOKING_CONFIRM_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsString(new UIData(trip, new PassengerDTO()))))
                .andExpect(status().isOk())
                .andExpect(content().string(BOOKING_CONFIRMED_MSG + BOOK_ID));
    }

    @Test
    public void shouldReturnSearchBookingView() throws Exception {
        mockMvc.perform(get(API_V2 + BOOKING_GET_PATH))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        mapper.writeValueAsString(new UIData().setBookingId(BOOK_ID_DISPLAY_DEFAULT))));
    }

    @Test
    public void shouldGetBookingDetails() throws Exception {
        BookingRecordDTO bookingRecordDTO = constructBookingDTO();

        when(bookClient.getBookingRecord(BOOK_ID)).thenReturn(bookingRecordDTO);

        mockMvc.perform(post(API_V2 + BOOKING_GET_DETAILS_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsBytes(new UIData(SearchQuery.getDefault()).setBookingId(BOOK_ID))))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(
                        new UIData(SearchQuery.getDefault())
                                .setPassenger(constructPassenger(bookingRecordDTO))
                                .setSelectedTrip(constructTrip(bookingRecordDTO))
                                .setBookingId(BOOK_ID))));
    }

    @Test
    public void bookQuery() throws Exception {
        CheckInRecordDTO checkInRecord = constructCheckInRecord();

        when(checkInClient.create(any())).thenReturn(CHECK_IN_ID);

        mockMvc.perform(post(API_V2 + BOOKING_CHECK_IN_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsString(checkInRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string(BOOKING_CHECK_IN_MSG + CHECK_IN_ID));
    }
}
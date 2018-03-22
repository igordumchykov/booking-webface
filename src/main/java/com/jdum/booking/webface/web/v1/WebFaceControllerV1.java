package com.jdum.booking.webface.web.v1;

import com.google.common.collect.Iterables;
import com.jdum.booking.common.dto.*;
import com.jdum.booking.common.exceptions.BusinessServiceException;
import com.jdum.booking.common.exceptions.NotFoundException;
import com.jdum.booking.webface.client.BookClient;
import com.jdum.booking.webface.client.CheckInClient;
import com.jdum.booking.webface.client.SearchClient;
import com.jdum.booking.webface.dto.UIData;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static com.jdum.booking.webface.constants.Constants.*;
import static com.jdum.booking.webface.constants.REST.*;

@Controller
@RequestMapping(API_V1)
@Slf4j
@RequiredArgsConstructor
public class WebFaceControllerV1 {

    @Autowired
    private CheckInClient checkInClient;

    @Autowired
    private SearchClient searchClient;

    @Autowired
    private BookClient bookClient;

    @GetMapping(INDEX_PATH)
    public String index(Model model) {
        model.addAttribute(UIDATA_ATTRIBUTE, new UIData(SearchQuery.getDefault()));
        return SEARCH_VIEW;
    }

    @PostMapping(TRIP_SEARCH_PATH)
    @HystrixCommand(fallbackMethod = "handleFeignError",
            ignoreExceptions = {NotFoundException.class, BusinessServiceException.class})
    public String searchTrip(@ModelAttribute(UIDATA_ATTRIBUTE) UIData uiData, Model model) {

        List<TripDTO> trips = searchClient.getTrips(uiData.getSearchQuery());
        uiData.setTrips(trips);
        model.addAttribute(UIDATA_ATTRIBUTE, uiData);

        return SEARCH_TRIP_RESULT_VIEW;
    }

    @PostMapping(BOOKING_BOOK_PATH)
    public String book(@ModelAttribute(TRIP_ATTRIBUTE) TripDTO trip, Model model) {
        model.addAttribute(UIDATA_ATTRIBUTE, new UIData(trip, new PassengerDTO()));
        return BOOKING_INPUT_VIEW;
    }

    @PostMapping(BOOKING_CONFIRM_PATH)
    public String confirmBooking(@ModelAttribute(UIDATA_ATTRIBUTE) UIData uiData, Model model) {

        TripDTO trip = uiData.getSelectedTrip();
        BookingRecordDTO booking = new BookingRecordDTO(trip);

        PassengerDTO passenger = uiData.getPassenger();
        passenger.setBookingRecord(booking);
        booking.setPassengers(newHashSet(passenger));

        Long bookingId = bookClient.create(booking);

        model.addAttribute(MESSAGE_ATTRIBUTE, BOOKING_CONFIRMED_MSG + bookingId);
        return BOOKING_CONFIRMATION_VIEW;
    }

    @GetMapping(BOOKING_GET_PATH)
    public String searchBooking(Model model) {

        UIData UIData = new UIData();
        UIData.setBookingId(BOOK_ID_DISPLAY_DEFAULT);//will be displayed
        model.addAttribute(UIDATA_ATTRIBUTE, UIData);

        return BOOKING_DETAILS_VIEW;
    }

    @PostMapping(BOOKING_GET_DETAILS_PATH)
    public String getBookingDetails(@ModelAttribute(UIDATA_ATTRIBUTE) UIData uiData, Model model) {

        Long id = uiData.getBookingId();
        BookingRecordDTO booking = bookClient.getBookingRecord(id);
        TripDTO trip = new TripDTO(booking);

        PassengerDTO passenger = Iterables.getFirst(booking.getPassengers(), null);
        uiData.setPassenger(passenger);
        uiData.setSelectedTrip(trip);
        uiData.setBookingId(id);
        model.addAttribute(UIDATA_ATTRIBUTE, uiData);

        return BOOKING_DETAILS_VIEW;
    }

    @PostMapping(BOOKING_CHECK_IN_PATH)
    public String checkInBookingRecord(@ModelAttribute(CHECK_IN_ATTRIBUTE) CheckInRecordDTO checkIn, Model model) {

        checkIn.setSeatNumber(SEAT_NUMBER);// TODO: 2/14/18 add logic to generate seat number automatically
        Long checkInId = checkInClient.create(checkIn);
        model.addAttribute(MESSAGE_ATTRIBUTE, BOOKING_CHECK_IN_MSG + checkInId);

        return CHECK_IN_CONFIRM_VIEW;
    }

    //Handles error using circuit breaker
    public String handleFeignError(@ModelAttribute(UIDATA_ATTRIBUTE) UIData uiData, Model model, Throwable exception) {
        model.addAttribute(ERROR_MODEL_NAME, exception);
        return INTERNAL_ERROR_VIEW_NAME;
    }
}
package com.jdum.booking.webface.constants;

/**
 * @author idumchykov
 * @since 2/21/18
 */
public interface Constants {

    String UIDATA_ATTRIBUTE = "uiData";
    String TRIP_ATTRIBUTE = "trip";
    String CHECK_IN_ATTRIBUTE = "checkIn";

    String BOOKING_CONFIRMED_MSG = "Your Booking is confirmed. Reference Number is ";
    String BOOKING_CHECK_IN_MSG = "Checked In, Seat Number is 28c, check in id is ";

    String SEARCH_VIEW = "search";
    String SEARCH_TRIP_RESULT_VIEW = "searchTripResult";
    String BOOKING_INPUT_VIEW = "bookInput";
    String BOOKING_CONFIRMATION_VIEW = "bookingConfirmation";
    String BOOKING_DETAILS_VIEW = "bookingDetails";
    String CHECK_IN_CONFIRM_VIEW = "checkinConfirm";
    String MESSAGE_ATTRIBUTE = "message";

    String NOT_FOUND_VIEW_NAME = "404";
    String BAD_REQUEST_VIEW_NAME = "400";
    String INTERNAL_ERROR_VIEW_NAME = "500";
    String ERROR_MODEL_NAME = "exception";

    long BOOK_ID_DISPLAY_DEFAULT = 1L;
    String SEAT_NUMBER = "28C";
}

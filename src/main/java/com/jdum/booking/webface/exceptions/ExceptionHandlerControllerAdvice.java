package com.jdum.booking.webface.exceptions;

import com.jdum.booking.common.exceptions.BusinessServiceException;
import com.jdum.booking.common.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import static com.jdum.booking.webface.constants.Constants.*;

/**
 * @author idumchykov
 * @since 2/19/18
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ModelAndView handle(NotFoundException exception) {
        log.error(exception.getMessage());
        return new ModelAndView(NOT_FOUND_VIEW_NAME, ERROR_MODEL_NAME, exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BusinessServiceException.class})
    public ModelAndView handle(BusinessServiceException exception) {
        log.error(exception.getMessage());
        return new ModelAndView(BAD_REQUEST_VIEW_NAME, ERROR_MODEL_NAME, exception);
    }
}

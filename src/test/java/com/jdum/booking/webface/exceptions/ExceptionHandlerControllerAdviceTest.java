package com.jdum.booking.webface.exceptions;

import com.jdum.booking.common.exceptions.BusinessServiceException;
import com.jdum.booking.common.exceptions.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import static com.jdum.booking.webface.constants.Constants.BAD_REQUEST_VIEW_NAME;
import static com.jdum.booking.webface.constants.Constants.ERROR_MODEL_NAME;
import static com.jdum.booking.webface.constants.Constants.NOT_FOUND_VIEW_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author idumchykov
 * @since 2/19/18
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ExceptionHandlerControllerAdviceTest {

    @SpyBean
    private ExceptionHandlerControllerAdvice exceptionHandlerControllerAdvice;

    @Test
    public void shouldReturn404View() {
        NotFoundException exception = new NotFoundException("Message");
        ModelAndView modelAndView = exceptionHandlerControllerAdvice.handle(exception);

        assertNotNull(modelAndView);
        assertEquals(NOT_FOUND_VIEW_NAME, modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get(ERROR_MODEL_NAME));
        assertEquals(exception, modelAndView.getModel().get(ERROR_MODEL_NAME));
    }

    @Test
    public void shouldReturn400View() {
        BusinessServiceException exception = new BusinessServiceException("Message");
        ModelAndView modelAndView = exceptionHandlerControllerAdvice.handle(exception);

        assertNotNull(modelAndView);
        assertEquals(BAD_REQUEST_VIEW_NAME, modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get(ERROR_MODEL_NAME));
        assertEquals(exception, modelAndView.getModel().get(ERROR_MODEL_NAME));
    }
}
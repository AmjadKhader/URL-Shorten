package url.shorten.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import url.shorten.service.exception.InvalidURLException;
import url.shorten.service.exception.URLNotFoundException;

@RestControllerAdvice
public class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidURLException.class)
    public String handleInvalidURLException(InvalidURLException e) {
        logger.error("Invalid URL ...", e);
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(URLNotFoundException.class)
    public String handleURLNotFoundException(URLNotFoundException e) {
        logger.error("URL not found ...", e);
        return e.getMessage();
    }
}

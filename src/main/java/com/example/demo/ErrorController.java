package com.example.demo;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Controller to handle invalid requests

@RestController
public class ErrorController extends AbstractErrorController {

	@Autowired
	public ErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

    private static final String ERROR_PATH=  "/error";

    //when a unknown path is requested the following method is called.
    
    @ExceptionHandler(NotFoundException.class)
    public HashMap<String,String> notFound() {
        return Util.getErrorOutput(Util.INVALID_REQUEST);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<?> handleErrors(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        if (status.equals(HttpStatus.NOT_FOUND))
            throw new NotFoundException();

        return ResponseEntity.status(status).body(getErrorAttributes(request,ErrorAttributeOptions.defaults()));
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}
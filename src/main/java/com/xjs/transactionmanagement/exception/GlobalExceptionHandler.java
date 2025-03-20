package com.xjs.transactionmanagement.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(ResponseStatusException e, Model model) {
        model.addAttribute("errorMessage", e.getReason());
        return new ModelAndView("error"); // 返回 error.html
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return new ModelAndView("error"); // 返回 error.html
    }
}
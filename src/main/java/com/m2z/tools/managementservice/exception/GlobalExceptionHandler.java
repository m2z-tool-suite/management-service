package com.m2z.tools.managementservice.exception;


import com.m2z.tools.managementservice.exception.dto.ValidationFailedResponseDto;
import com.m2z.tools.managementservice.generic.GenericResponseDTO;
import com.m2z.tools.managementservice.security.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponseDTO handleExceptions(UnsupportedOperationException exception, WebRequest webRequest) {
        log.error("Unsupported operation accessed on URL: {}", webRequest.getContextPath());
        return GenericResponseDTO.bad();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponseDTO handleExceptions(Exception exception, WebRequest webRequest) {
        log.error("Unhandled error name: {} message {}", exception.getClass().getName(), exception.getMessage(), exception);
        return GenericResponseDTO.bad();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponseDTO handleExceptions(RuntimeException exception, WebRequest webRequest) {
        log.error("Unhandled RuntimeException: {} name: {} message {}", exception.getCause(), exception.getClass().getName(), exception.getMessage(), exception);
        return GenericResponseDTO.bad();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public GenericResponseDTO handleExceptions(AccessDeniedException exception, HttpServletRequest r) {
        log.warn("Access denied for user: {} path: {}", SecurityUtil.getSecurityContextUser().getUserId(), r.getRequestURI());
        return new GenericResponseDTO("Forbidden", HttpStatus.FORBIDDEN.value());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ValidationFailedResponseDto dto = new ValidationFailedResponseDto(ex);
        log.debug("Validation constraint violation count: {} errors: {}", ex.getErrorCount(), dto.getValidation());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}

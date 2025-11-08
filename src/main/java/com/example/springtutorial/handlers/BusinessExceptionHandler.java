package com.example.springtutorial.handlers;

import com.example.springtutorial.dto.BaseResponse;
import com.example.springtutorial.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<?>> handleBusinessException(BusinessException ex) {
        logRootError(
                ex.getStackTrace(),
                "[Guideline] : Business Exception in {}.{}():{} - {}",
                ex.getMessage(),
                "[Guideline] : Business Exception: {}"
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(
                        BaseResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(ex.getMessage())
                                .errorCode(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<BaseResponse<?>> handleServerErrorException(Exception ex) {
        logRootError(
                ex.getStackTrace(),
                "[Guideline] : Internal server error in {}.{}():{} - {}",
                ex.getMessage(),
                "[Guideline] : Internal server error: {}"
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        BaseResponse.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(ex.getMessage())
                                .errorCode("SOMETHING_WENT_WRONG")
                                .build()
                );
    }

    private static void logRootError(StackTraceElement[] stackTrace, String format, String message, String defaultFormat) {
        if (stackTrace.length > 0) {
            StackTraceElement origin = stackTrace[0];
            String className = origin.getClassName();
            String methodName = origin.getMethodName();
            int lineNumber = origin.getLineNumber();
            log.error(format,
                    className, methodName, lineNumber, message);
        } else {
            log.error(defaultFormat, message);
        }
    }
}

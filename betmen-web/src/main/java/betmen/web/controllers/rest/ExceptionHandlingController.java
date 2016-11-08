package betmen.web.controllers.rest;

import betmen.core.exception.BadRequestException;
import betmen.core.exception.BusinessException;
import betmen.core.exception.UnprocessableEntityException;
import betmen.dto.dto.error.CommonErrorResource;
import betmen.dto.dto.error.CommonErrorResponse;
import betmen.dto.dto.error.FieldErrorResource;
import betmen.dto.dto.error.FieldErrorsResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class ExceptionHandlingController {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public void dtoValidationException(final Exception e, final Writer writer) throws IOException {
        List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        writer.write(new Gson().toJson(new FieldErrorsResponse(wrapFieldErrors(fieldErrors))));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public void dtoBindException(final Exception e, final Writer writer) throws IOException {
        List<FieldError> fieldErrors = ((BindException) e).getBindingResult().getFieldErrors();
        writer.write(new Gson().toJson(new FieldErrorsResponse(wrapFieldErrors(fieldErrors))));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    public void badRequestException(final Exception e, final Writer writer) throws IOException {
        String error = String.format("%s: %s", e.getClass().getName(), e.getMessage());
        writer.write(new Gson().toJson(new CommonErrorResponse(new CommonErrorResource(error, ""))));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = UnprocessableEntityException.class)
    public void unprocessableEntityException(final Exception e, final Writer writer) throws IOException {
        writer.write(new Gson().toJson(new CommonErrorResponse(new CommonErrorResource(e.getMessage(), ""))));
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public void internalServerError(final Exception e, final Writer writer) throws IOException {
        String error = String.format("%s: %s", e.getClass().getName(), e.getMessage());
        writer.write(new Gson().toJson(new CommonErrorResponse(new CommonErrorResource(error, ""))));
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(value = BusinessException.class)
    public void businessException(final BusinessException e, final Writer writer) throws IOException {
        writer.write(new Gson().toJson(new CommonErrorResponse(new CommonErrorResource(e.getErrorCode(), e.getTranslatedMessage()))));
    }

    private Map<String, List<FieldErrorResource>> wrapFieldErrors(final List<FieldError> fieldErrors) {
        List<FieldErrorResource> response = fieldErrors.stream()
                .map(fieldError -> new FieldErrorResource(
                        fieldError.getField(),
                        fieldError.getRejectedValue().toString(),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.toList()
                );
        return response.stream()
                .collect(Collectors.groupingBy(FieldErrorResource::getField));
    }
}

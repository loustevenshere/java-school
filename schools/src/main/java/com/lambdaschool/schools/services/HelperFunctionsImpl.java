package com.lambdaschool.schools.services;

import com.lambdaschool.schools.models.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunctions")
public class HelperFunctionsImpl implements HelperFunctions
{

    @Override
    public List<ValidationError> getConstraintViolation(Throwable cause) {

        while ((cause != null) && (cause instanceof org.hibernate.exception.ConstraintViolationException ||
                cause instanceof MethodArgumentNotValidException))
        {
            cause = cause.getCause();
        }

        List<ValidationError> listVe = new ArrayList<>();

        if (cause != null)
        {
            if (cause instanceof org.hibernate.exception.ConstraintViolationException)
            {
                org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) cause;
                ValidationError newVe = new ValidationError();
                newVe.setCode(ex.getMessage());
                newVe.setMessage(ex.getConstraintName());
                listVe.add(newVe);
            } else {
                MethodArgumentNotValidException ex = (MethodArgumentNotValidException) cause;
                List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
                for (FieldError err : fieldErrors)
                {
                    ValidationError newVe = new ValidationError();
                    newVe.setCode(err.getField());
                    newVe.setMessage(err.getDefaultMessage());
                    listVe.add(newVe);
                }
            }
        }


        return listVe;
    }
}

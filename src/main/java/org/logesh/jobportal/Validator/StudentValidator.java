package org.logesh.jobportal.Validator;

import org.logesh.jobportal.Model.Application;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class StudentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Application.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Application application = (Application) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "jobId", "jobId.empty", "Job ID is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "studentEmail", "studentEmail.empty", "Student Email is required");

        if (application.getStatus() == null || application.getStatus().isEmpty()) {
            errors.rejectValue("status", "status.empty", "Status cannot be empty");
        }
    }
}

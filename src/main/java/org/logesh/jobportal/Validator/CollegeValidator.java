package org.logesh.jobportal.Validator;

import org.logesh.jobportal.Model.Application;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CollegeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Application.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Application application = (Application) target;

        // Validate Application ID
        if (application.getId() <= 0) {
            errors.rejectValue("id", "invalid-id", "Application ID must be a positive number.");
        }

        // Validate Student Email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "studentEmail", "empty-email", "Student email cannot be empty.");

        // Validate Status
        if (application.getStatus() == null ||
                (!application.getStatus().equalsIgnoreCase("approve") &&
                        !application.getStatus().equalsIgnoreCase("reject"))) {
            errors.rejectValue("status", "invalid-status", "Status must be 'approve' or 'reject'.");
        }
    }
}

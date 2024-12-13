package org.logesh.jobportal.Validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ResumeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile file = (MultipartFile) target;

        // Check if file is empty
        if (file.isEmpty()) {
            errors.rejectValue("file", "file.empty", "Resume file cannot be empty.");
        }

        // Check file size (e.g., max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            errors.rejectValue("file", "file.size", "Resume file must be less than 5MB.");
        }

        // Check file type (e.g., only PDF or DOCX)
        String contentType = file.getContentType();
        if (contentType != null && !contentType.equals("application/pdf") && !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            errors.rejectValue("file", "file.type", "Only PDF and DOCX files are allowed.");
        }
    }
}

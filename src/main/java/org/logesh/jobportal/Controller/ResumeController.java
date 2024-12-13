package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.logesh.jobportal.Dao.ResumeDao;
import org.logesh.jobportal.Model.Resume;
import org.logesh.jobportal.Validator.ResumeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ResumeController {

    @Autowired
    ResumeDao resumeDao;

    @Autowired
    ResumeValidator resumeValidator;

    // Handle resume upload
    @PostMapping("/resume/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file,
                               ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("studentEmail");
        Errors errors = new BeanPropertyBindingResult(file, "file");
        resumeValidator.validate(file, errors);
        resumeValidator.validate(file, errors);
        if (email == null) {
            map.addAttribute("errorMessage", "Session expired. Please log in again.");
            return "redirect:/login";
        }
        if (errors.hasErrors()) {
            map.addAttribute("errorMessage", "Invalid resume file. Please check the requirements.");
            map.addAttribute("validationErrors", errors.getAllErrors());
            return "Student/student-resume"; // Return to the form with validation errors
        }
        try {
            Resume resume = new Resume();
            resume.setStudentEmail(email);
            resume.setFileName(file.getOriginalFilename());
            resume.setContentType(file.getContentType());
            resume.setFileData(file.getBytes());

            resumeDao.saveResume(resume);
            map.addAttribute("successMessage", "Resume uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            map.addAttribute("errorMessage", "Error uploading resume.");
        }
        return "Student/student-resume"; // Adjust the view to only handle uploads
    }

    // View resume page
    @GetMapping("/resume/view")
    public String viewResume(ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("studentEmail");
        if (email == null) {
            map.addAttribute("errorMessage", "Session expired. Please log in again.");
            return "redirect:/login";
        }

        Resume resume = resumeDao.getResumeByEmail(email);
        if (resume != null) {
            map.addAttribute("resume", resume);
        } else {
            map.addAttribute("errorMessage", "Resume not found.");
        }
        return "Student/student-resume"; // Adjusted view for displaying resume details
    }

    // Handle resume download
    @GetMapping("/resume/download")
    public ResponseEntity<byte[]> downloadResume(HttpSession session) {
        String email = (String) session.getAttribute("studentEmail");
        if (email == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Resume resume = resumeDao.getResumeByEmail(email);
        if (resume != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, resume.getContentType())
                    .body(resume.getFileData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import org.logesh.jobportal.Dao.ResumeDao;
import org.logesh.jobportal.Model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ResumeController {

    @Autowired
    ResumeDao resumeDao;

    @PostMapping("/resume/upload")
    public String uploadResume(@RequestParam("email") String email,
                               @RequestParam("file") MultipartFile file,
                               ModelMap map) {
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
        return "Student/student-resume";
    }

    @GetMapping("/resume/view")
    public String viewResume(ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("studentEmail");
        Resume resume = resumeDao.getResumeByEmail(email);
        if (resume != null) {
            map.addAttribute("resume", resume);
        } else {
            map.addAttribute("errorMessage", "Resume not found.");
        }
        return "Student/student-resume";
    }
}

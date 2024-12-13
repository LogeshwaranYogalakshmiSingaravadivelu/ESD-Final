package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.logesh.jobportal.Dao.*;
import org.logesh.jobportal.Model.Application;
import org.logesh.jobportal.Model.Job;
import org.logesh.jobportal.Model.Resume;
import org.logesh.jobportal.Validator.CollegeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CollegeController {

    @Autowired
    private CollegeValidator collegeValidator;

    @Autowired
    CollegeDao collegeDao;

    @Autowired
    JobDao jobDao;

    @Autowired
    ApplicationDao applicationDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ResumeDao resumeDao;

    @GetMapping("/admin")
    public String handleHome(ModelMap map) {
        List<Application> pendingApplications = collegeDao.getPendingApprovals("Waiting");
        List<Map<String, Object>> detailedApplications = new ArrayList<>();
        for(Application app : pendingApplications) {
            String studentEmail = app.getStudentEmail();
            int jobId = app.getJobId();
            String studentName = userDao.getUsernameByEmail(studentEmail);
            Job job = jobDao.getJobById(jobId);
            Resume resume = resumeDao.getResumeByEmail(studentEmail);
            Map<String, Object> applicationDetails = new HashMap<>();
            applicationDetails.put("jobTitle", job.getTitle());
            applicationDetails.put("jobDescription", job.getDescription());
            applicationDetails.put("studentEmail", studentEmail);
            applicationDetails.put("studentName", studentName);
            applicationDetails.put("applicationId", app.getId());
            applicationDetails.put("status", app.getStatus());
            if (resume != null) {
                applicationDetails.put("resumeFileName", resume.getFileName());
                applicationDetails.put("resumeContentType", resume.getContentType());
                applicationDetails.put("resumeId", resume.getId()); // Assuming Resume has an ID
            } else {
                applicationDetails.put("resumeFileName", "No resume uploaded");
            }
            detailedApplications.add(applicationDetails);
        }
        map.addAttribute("applications", detailedApplications);
        return "College/home";
    }

    @PostMapping("/admin/approval")
    public String handleApproval(
            @Valid Application application,
            BindingResult bindingResult,
            ModelMap map,
            @RequestParam("applicationId") int applicationId,
            @RequestParam("decision") String decision,
            HttpSession session) {

        collegeValidator.validate(application, bindingResult);

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            map.addAttribute("errors", bindingResult.getAllErrors());
            return "College/home"; // Return to the form with error messages
        }

        Application existingApplication = applicationDao.getApplicationById(applicationId);

        if (existingApplication != null) {
            existingApplication.setStatus(decision.equals("approve") ? "Approved" : "Rejected");
            applicationDao.updateApplication(existingApplication);
        }

        return "College/home"; // Redirect back to the dashboard
    }
}

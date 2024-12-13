package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import org.logesh.jobportal.Dao.*;
import org.logesh.jobportal.Model.Application;
import org.logesh.jobportal.Model.College;
import org.logesh.jobportal.Model.Job;
import org.logesh.jobportal.Model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
            @RequestParam("applicationId") int applicationId,
            @RequestParam("decision") String decision,
            HttpSession session) {
        Application application = applicationDao.getApplicationById(applicationId);

        if (application != null) {
            application.setStatus(decision.equals("approve") ? "Approved" : "Rejected");
            applicationDao.updateApplication(application);
        }

        return "College/home"; // Redirect back to the dashboard
    }
}

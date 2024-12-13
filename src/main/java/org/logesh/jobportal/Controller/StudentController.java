package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.logesh.jobportal.Dao.ApplicationDao;
import org.logesh.jobportal.Dao.JobDao;
import org.logesh.jobportal.Model.Application;
import org.logesh.jobportal.Model.Job;
import org.logesh.jobportal.Validator.StudentValidator;
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
public class StudentController {

    @Autowired
    JobDao jobDao;

    @Autowired
    ApplicationDao applicationDao;

    @Autowired
    StudentValidator studentValidator;

    @GetMapping("/student")
    public String jobList(Application application, ModelMap map, HttpSession session) {

        List<Job> allJobs = jobDao.getAllJobs();
        String email = (String) session.getAttribute("studentEmail");
        System.out.println(allJobs);
        List<Application> userApplications = applicationDao.getApplicationsByStudentEmail(email);
        System.out.println(userApplications);
        List<Job> availableJobs = new ArrayList<>();

        for (Job job : allJobs) {
            boolean isApplied = false;
            for (Application app : userApplications) {
                if (app.getJobId() == job.getId()) {
                    isApplied = true;
                    break; // Exit inner loop if the job is already applied
                }
            }
            if (!isApplied) {
                availableJobs.add(job);
            }
        }
        System.out.println(availableJobs);

        map.addAttribute("jobs", availableJobs);
        map.addAttribute("student", email);
        map.addAttribute("application", application);
        return "Student/home";
    }

    @PostMapping("/apply")
    public String applyForJob(@Valid Application application, @RequestParam("jobId") int jobId, @RequestParam("studentEmail") String studentEmail, BindingResult bindingResult, ModelMap map) {
        // Save application
        studentValidator.validate(studentEmail, bindingResult);

        if (bindingResult.hasErrors()) {
            map.addAttribute("errors", bindingResult.getAllErrors());
            return "Student/home";
        }

        Application Jobapplication = new Application();
        Jobapplication.setJobId(jobId);
        Jobapplication.setStatus("Applied");
        Jobapplication.setStudentEmail(studentEmail);
        String recruiterEmail = jobDao.getRecruiterEmail(jobId);
        Jobapplication.setRecruiterEmail(recruiterEmail);
        applicationDao.saveApplication(Jobapplication);
        map.addAttribute("success", "Application submitted successfully.");

        return "redirect:/student?email=" + studentEmail;
    }

    @GetMapping("/applied")
    public String appliedJobs(Application application, ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("studentEmail");
        List<Application> userApplications = applicationDao.getApplicationsByStudentEmail(email);

        List<Map<String, Object>> appliedJobsList = new ArrayList<>();

        for (Application app : userApplications) {
            int jobId = app.getJobId();
            Job job = jobDao.getJobById(jobId);
            if (job != null) {
                Map<String, Object> jobDetails = new HashMap<>();
                jobDetails.put("title", job.getTitle());
                jobDetails.put("description", job.getDescription());
                jobDetails.put("location", job.getLocation());
                jobDetails.put("status", app.getStatus());
                jobDetails.put("applicationId", app.getId());

                appliedJobsList.add(jobDetails);
            }
        }

        map.addAttribute("jobApplication", appliedJobsList);
        return "Student/student-applied";
    }

    @PostMapping("/student/approval")
    public String handleStudentApproval(
            @RequestParam("applicationId") int applicationId,
            HttpSession session) {

        // Fetch the application by ID
        Application application = applicationDao.getApplicationById(applicationId);

        if (application != null && "Selected".equals(application.getStatus())) {
            // Update the status to "Waiting"
            application.setStatus("Waiting");
            applicationDao.updateApplication(application);
        }

        // Redirect back to the applied jobs page
        return "redirect:/applied";
    }



}

package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import org.logesh.jobportal.Dao.ApplicationDao;
import org.logesh.jobportal.Dao.JobDao;
import org.logesh.jobportal.Model.Application;
import org.logesh.jobportal.Model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    JobDao jobDao;

    @Autowired
    ApplicationDao applicationDao;

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
    public String applyForJob(@RequestParam("jobId") int jobId, @RequestParam("studentEmail") String studentEmail, ModelMap map) {
        // Save application
        Application application = new Application();
        application.setJobId(jobId);
        application.setStudentEmail(studentEmail);
        String recruiterEmail = jobDao.getRecruiterEmail(jobId);
        application.setRecruiterEmail(recruiterEmail);
        applicationDao.saveApplication(application);
        map.addAttribute("success", "Application submitted successfully.");

        return "redirect:/student?email=" + studentEmail;
    }

    @GetMapping("/applied")
    public String appliedJobs(Application application, ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("studentEmail");
        List<Application> userApplications = applicationDao.getApplicationsByStudentEmail(email);

        List<Job> appliedJobsList = new ArrayList<>();

        for (Application app : userApplications) {
            int id = app.getJobId();
            Job job = jobDao.getJobById(id);
            appliedJobsList.add(job);
        }

        map.addAttribute("jobApplication", appliedJobsList);
        return "Student/student-applied";
    }


}

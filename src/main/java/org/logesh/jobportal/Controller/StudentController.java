package org.logesh.jobportal.Controller;

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

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    JobDao jobDao;

    @Autowired
    ApplicationDao applicationDao;

    @GetMapping("/student")
    public String jobList(Application application, ModelMap map) {
        map.addAttribute("application", application);
        return "Student/home";
    }

//    @GetMapping("/student-home")
//    public String studentHome(@RequestParam("email") String email, ModelMap map) {
//        // Get all job postings
//        List<Job> allJobs = jobDao.getAllJobs();
//        map.addAttribute("jobs", allJobs);
//
//        // Get jobs the student has applied to
//        List<Application> applications = applicationDao.getApplicationsByStudentEmail(email);
//        map.addAttribute("applications", applications);
//
//        // Pass the student email to the view for reuse
//        map.addAttribute("studentEmail", email);
//
//        return "Student/home";
//    }

    @PostMapping("/apply")
    public String applyForJob(@RequestParam("jobId") Long jobId, @RequestParam("studentEmail") String studentEmail) {
        // Save application
        Application application = new Application();
        application.setJobId(jobId);
        application.setStudentEmail(studentEmail);

        applicationDao.saveApplication(application);

        return "redirect:/student-home?email=" + studentEmail;
    }


}

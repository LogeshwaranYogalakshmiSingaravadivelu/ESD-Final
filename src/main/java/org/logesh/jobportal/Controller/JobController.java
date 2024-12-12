package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import org.logesh.jobportal.Dao.JobDao;
import org.logesh.jobportal.Model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class JobController {

    @Autowired
    JobDao jobDao;

    @GetMapping("/job")
    public String jobList(Job job, ModelMap map) {
        map.addAttribute("job", job);
        return "Recruiters/home";
    }

    @PostMapping("/job")
    public String handleForm(@ModelAttribute Job job, ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("recruiterEmail");
        job.setRecruiterEmail(email);
        jobDao.saveJob(job);
        map.addAttribute("successMessage", "Job posted successfully!");
        map.addAttribute("job", job);
        return "Recruiters/home";
    }

    @GetMapping("/posted")
    public String showAllJobsByRecruiter(ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("recruiterEmail");
        List<Job> jobs = jobDao.getAllJobsByRecruiter(email);
        map.addAttribute("jobs", jobs);
        return "Recruiters/posted-jobs"; // Adjust the view name accordingly
    }

//    @GetMapping("/manage")
//    public String ManageApplication(ModelMap map, HttpSession session) {
//        String email = (String) session.getAttribute("recruiterEmail");
//        List<Job> jobs = jobDao.getStudents(email);
//        map.addAttribute("jobs", jobs);
//        return "Recruiters/posted-jobs"; // Adjust the view name accordingly
//    }

    @PostMapping("/job/delete")
    public String deleteJob(@ModelAttribute("id") int jobId, ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("recruiterEmail");
        jobDao.deleteJobById(jobId);
        List<Job> jobs = jobDao.getAllJobsByRecruiter(email);
        map.addAttribute("jobs", jobs);
        return "Recruiters/posted-jobs";
    }

    @PostMapping("/job/edit")
    public String editJob(@ModelAttribute Job job, ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("recruiterEmail");
        job.setRecruiterEmail(email);
        jobDao.updateJob(job);
        List<Job> jobs = jobDao.getAllJobsByRecruiter(email);
        map.addAttribute("jobs", jobs);
        return "Recruiters/posted-jobs";
    }


}

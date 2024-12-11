package org.logesh.jobportal.Controller;

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
    public String handleForm(@ModelAttribute Job job, @RequestParam String recruiterName, ModelMap map) {
        job.setRecruiterName(recruiterName);
        jobDao.saveJob(job);
        map.addAttribute("successMessage", "Job posted successfully!");
        map.addAttribute("job", job);
        return "Recruiters/home";
    }

    @GetMapping("/posted")
    public String showAllJobs(ModelMap map) {
        List<Job> jobs = jobDao.getAllJobs();
        map.addAttribute("jobs", jobs);
        return "Recruiters/posted-jobs"; // Adjust the view name accordingly
    }

    @PostMapping("/job/delete")
    public String deleteJob(@ModelAttribute("id") Long jobId, ModelMap map) {
        jobDao.deleteJobById(jobId);
        List<Job> jobs = jobDao.getAllJobs();
        map.addAttribute("jobs", jobs);
        return "Recruiters/posted-jobs";
    }

    @PostMapping("/job/edit")
    public String editJob(@ModelAttribute Job job, ModelMap map) {
        jobDao.updateJob(job);
        List<Job> jobs = jobDao.getAllJobs();
        map.addAttribute("jobs", jobs);
        return "Recruiters/posted-jobs";
    }


}

package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import org.logesh.jobportal.Dao.ApplicationDao;
import org.logesh.jobportal.Dao.JobDao;
import org.logesh.jobportal.Dao.ResumeDao;
import org.logesh.jobportal.Dao.UserDao;
import org.logesh.jobportal.Model.Application;
import org.logesh.jobportal.Model.Job;
import org.logesh.jobportal.Model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JobController {

    @Autowired
    JobDao jobDao;

    @Autowired
    ApplicationDao applicationDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ResumeDao resumeDao;

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

    @GetMapping("/decision")
    public String ManageApplication(ModelMap map, HttpSession session) {
        String email = (String) session.getAttribute("recruiterEmail");
        List<Object[]> jobs = applicationDao.getStudentsEmail(email);
        List<Map<String, Object>> detailedApplications = new ArrayList<>();

        for (Object[] jobApplication : jobs) {
            int jobId = (int) jobApplication[0];
            String studentEmail = (String) jobApplication[1];
            String status = (String) jobApplication[2];
            int applicationId = (int) jobApplication[3];

            Job job = jobDao.getJobById(jobId);

            String studentName = userDao.getUsernameByEmail(studentEmail);
            Resume resume = resumeDao.getResumeByEmail(studentEmail);

            Map<String, Object> applicationDetails = new HashMap<>();
            applicationDetails.put("jobTitle", job.getTitle());
            applicationDetails.put("jobDescription", job.getDescription());
            applicationDetails.put("studentEmail", studentEmail);
            applicationDetails.put("studentName", studentName);
            applicationDetails.put("status", status);
            applicationDetails.put("applicationId", applicationId);

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
        return "Recruiters/jobs-decision";
    }

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

    @PostMapping("/recruiter/application/decision")
    public String handleDecision(@RequestParam("applicationId") int applicationId, @RequestParam("decision") String decision, ModelMap map,
            HttpSession session) {
        String email = (String) session.getAttribute("recruiterEmail");
        Application application = applicationDao.getApplicationById(applicationId);
        if (application != null) {
            // Update the status based on the decision
            if ("accept".equalsIgnoreCase(decision)) {
                application.setStatus("Selected");
            } else if ("reject".equalsIgnoreCase(decision)) {
                application.setStatus("Rejected");
            }

            applicationDao.updateApplication(application);
        }

        List<Object[]> jobApplications = applicationDao.getStudentsEmail(email);
        List<Map<String, Object>> detailedApplications = prepareApplicationDetails(jobApplications);
        map.addAttribute("applications", detailedApplications);

        return "Recruiters/jobs-decision";
    }


    private List<Map<String, Object>> prepareApplicationDetails(List<Object[]> jobApplications) {
        List<Map<String, Object>> detailedApplications = new ArrayList<>();

        for (Object[] jobApplication : jobApplications) {
            int jobId = (int) jobApplication[0];
            String studentEmail = (String) jobApplication[1];
            String status = (String) jobApplication[2];
            int applicationId = (int) jobApplication[3];

            Job job = jobDao.getJobById(jobId);
            String studentName = userDao.getUsernameByEmail(studentEmail);
            Resume resume = resumeDao.getResumeByEmail(studentEmail);

            Map<String, Object> applicationDetails = new HashMap<>();
            applicationDetails.put("jobTitle", job.getTitle());
            applicationDetails.put("jobDescription", job.getDescription());
            applicationDetails.put("studentName", studentName);
            applicationDetails.put("studentEmail", studentEmail);
            applicationDetails.put("resumeFileName", resume != null ? resume.getFileName() : null);
            applicationDetails.put("resumeId", resume != null ? resume.getId() : null);
            applicationDetails.put("status", status);
            applicationDetails.put("applicationId", applicationId);

            detailedApplications.add(applicationDetails);
        }

        return detailedApplications;
    }


}

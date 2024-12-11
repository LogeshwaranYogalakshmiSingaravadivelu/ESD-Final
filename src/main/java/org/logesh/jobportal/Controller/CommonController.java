package org.logesh.jobportal.Controller;

import org.logesh.jobportal.Dao.StudentDao;
import org.logesh.jobportal.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommonController {

    @Autowired
    StudentDao studentDao;

    @GetMapping("/")
    public String login(Student student, ModelMap map) {
        map.addAttribute("student", student);
        return "Common/index";
    }

    @PostMapping("/")
    public String handleForm(@ModelAttribute Student student, ModelMap map) {
        Student existingStudent = studentDao.findByEmail(student.getEmail());

        if (existingStudent != null) {
            // Store the user in the session
            map.addAttribute("student", existingStudent);

            // Redirect based on type
            if ("Recruiter".equalsIgnoreCase(existingStudent.getType())) {
                return "Recruiters/home";
            } else if ("Student".equalsIgnoreCase(existingStudent.getType())) {
                return "redirect:/student-home";
            }
        }
        map.addAttribute("error", "Invalid email or user not found!");
        return "Common/index";
    }

    @GetMapping("/signup")
    public String signup(Student student, ModelMap map) {
        map.addAttribute("student", student);
        return "Common/signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute Student student, ModelMap map) {
        studentDao.saveUser(student);
        map.addAttribute("student", student);
        return "Common/index";
    }
}

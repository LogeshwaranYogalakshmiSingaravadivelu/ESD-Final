package org.logesh.jobportal.Controller;

import org.logesh.jobportal.Dao.UserDao;
import org.logesh.jobportal.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommonController {

    @Autowired
    UserDao userDao;

    @GetMapping("/")
    public String login(User user, ModelMap map) {
        map.addAttribute("student", user);
        return "Common/index";
    }

    @PostMapping("/")
    public String handleForm(@ModelAttribute User user, ModelMap map) {
        User existingUser = userDao.findByEmail(user.getEmail());

        if (existingUser != null) {
            // Store the user in the session
            map.addAttribute("student", existingUser);

            // Redirect based on type
            if ("Recruiter".equalsIgnoreCase(existingUser.getType())) {
                return "redirect:/job";
            } else if ("User".equalsIgnoreCase(existingUser.getType())) {
                return "redirect:/student-home";
            }
        }
        map.addAttribute("error", "Invalid email or user not found!");
        return "Common/index";
    }

    @GetMapping("/signup")
    public String signup(User user, ModelMap map) {
        map.addAttribute("student", user);
        return "Common/signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user, ModelMap map) {
        userDao.saveUser(user);
        map.addAttribute("student", user);
        return "redirect:/";
    }
}

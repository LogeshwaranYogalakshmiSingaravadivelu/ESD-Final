package org.logesh.jobportal.Controller;

import jakarta.servlet.http.HttpSession;
import org.logesh.jobportal.Dao.UserDao;
import org.logesh.jobportal.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String handleForm(@ModelAttribute User user, ModelMap map, RedirectAttributes redirectAttributes, HttpSession session) {
        User existingUser = userDao.findByEmail(user.getEmail());

        if (existingUser != null) {
            // Store the user in the session
            map.addAttribute("student", existingUser);

            // Redirect based on type
            if ("Recruiter".equalsIgnoreCase(existingUser.getType())) {
                session.setAttribute("recruiterEmail", user.getEmail());
                return "redirect:/job";
            } else if ("Student".equalsIgnoreCase(existingUser.getType())) {
                session.setAttribute("studentEmail", user.getEmail());
                return "redirect:/student";
            } else if ("Admin".equalsIgnoreCase(existingUser.getType())) {
                session.setAttribute("adminEmail", user.getEmail());
                return "redirect:/admin";
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

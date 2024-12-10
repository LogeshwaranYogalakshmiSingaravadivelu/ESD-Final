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
public class MyController {

    @Autowired
    UserDao userDao;

    @GetMapping("/hello")
    public String hello(User user, ModelMap map) {
        map.addAttribute("user", user);
        return "hello";
    }

    @PostMapping("/hello")
    public String handleForm(@ModelAttribute User user, ModelMap map) {
        userDao.saveUser(user);
        map.addAttribute("user", user);
        return "user-added";
    }

    @GetMapping("/table")
    public String showTable() {
        return "ajax-view";
    }
}

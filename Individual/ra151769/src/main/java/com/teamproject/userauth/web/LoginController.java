package com.teamproject.userauth.web;

import com.teamproject.userauth.model.User;
import com.teamproject.userauth.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Controller
public class LoginController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/home")
    public String home(){
        return "index99";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "resetPassword";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }

    @GetMapping(path = "/resetPasswordReceive")
    public String resetUserPassword(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password){

        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        System.out.println("-----------");

        User profile = userRepository.findByUsername(username);

        System.out.println("IN Registration");
        System.out.println("User username: " + profile.getUsername());

        password = passwordEncoder.encode(password);

        if ((profile.getUsername().equals(username)) && (profile.getEmail().equals(email))){
            profile.setPassword(password);
            userRepository.save(profile);

            return "redirect:/login";
        }
        else{
            return "redirect:/login?error";
        }
    }
}

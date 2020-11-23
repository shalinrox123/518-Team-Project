package com.teamproject.userauth.web;

import com.teamproject.userauth.model.User;
import com.teamproject.userauth.repository.UserRepository;
import com.teamproject.userauth.service.UserService;
import com.teamproject.userauth.web.dto.UserRegistrationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(){
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto){
        String currentName = registrationDto.getUserName();

        if(userService.findByUsername(currentName) != null){
            return "redirect:/registration?failure";
        }
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "resetPassword";
    }

    @PostMapping(path = "/resetPasswordReceive")
    public String resetUserPassword(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password){
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        System.out.println("---------------");

        User profile = userRepository.findByUsername(username);
        
        System.out.println("IN Registration");
        System.out.println("User username: " + profile.getUsername());

        password = passwordEncoder.encode(password);

        if ((profile.getUsername().equals(username)) && (profile.getEmail().equals(email))){
            profile.setPassword(password);
            userRepository.save(profile);

            return "redirect:/login?passwordReset";
        }
        else{
            return "redirect:/login?error";
        }
    }
}

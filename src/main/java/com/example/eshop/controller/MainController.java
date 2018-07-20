package com.example.eshop.controller;

import com.example.eshop.model.*;
import com.example.eshop.repository.BrandRepository;
import com.example.eshop.repository.CategoryRepository;
import com.example.eshop.repository.ProductRepository;
import com.example.eshop.repository.UserRepository;
import com.example.eshop.security.CurrentUser;
import com.example.eshop.util.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;


@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EmailServiceImpl emailService;

    @GetMapping("/homePage")
    public String userHome(ModelMap map) {
        map.addAttribute("products", productRepository.findAll());
        map.addAttribute("brands",brandRepository.findAll());
        map.addAttribute("brand",new Brand());
        map.addAttribute("categories", categoryRepository.findAll());
        map.addAttribute("category", new Category());
        map.addAttribute("product", new Product());
        return "index";
    }
    @PostMapping("/addUser")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result){
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            for (ObjectError objectError : result.getAllErrors()) {
                sb.append(objectError.getDefaultMessage() + "<br>");
            }
            return "redirect:/homePage?message=" + sb.toString();
        }
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user);

        String url = String.format("http://localhost:8080/verify?token=%s&email=%s", user.getToken(), user.getEmail());
        String text = String.format("Dear %s Thank you, you have successfully registered to our EShop, Please visit by link in order to activate your profile. %s", user.getName(), url);
        emailService.sendSimpleMessage(user.getEmail(), "Welcome", text);
        return "redirect:/homePage";

    }
    @RequestMapping( value = "/verify", method = RequestMethod.GET)
    public  String verify(@RequestParam("token")String token, @RequestParam("email")String email){
        User oneByEmail = userRepository.findOneByEmail(email);
        if (oneByEmail!=null){
            if (oneByEmail.getToken()!=null&&oneByEmail.getToken().equals(token)){
                oneByEmail.setToken(null);
                oneByEmail.setVerify(true);
                userRepository.save(oneByEmail);
            }
        }
return "redirect:/homePage";
    }


    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
    public String login( ModelMap map) {
        map.addAttribute("users", userRepository.findAll());
        map.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
    public String loginSuccess() {
//        CurrentUser currentUser= new CurrentUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getUser().getRole() == UserRole.ADMIN) {
            return "redirect:/admin";
        }
        return "redirect:/homePage";
    }

    @GetMapping("/login")

    public String log( ModelMap map){
        map.addAttribute("users", userRepository.findAll());
        map.addAttribute("user", new User());
        return "login";
    }
@GetMapping("/productDetails")
public  String productDetails(@RequestParam ("id") int id, ModelMap map){
map.addAttribute("products", productRepository.findAll());
    Product product = productRepository.findOneById(id);
    map.addAttribute("product", product);
        return "productDetails";
}
}
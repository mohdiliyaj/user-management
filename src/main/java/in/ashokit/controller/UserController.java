package in.ashokit.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.RegistrationForm;
import in.ashokit.binding.ResetPassword;
import in.ashokit.entity.Country;
import in.ashokit.entity.State;
import in.ashokit.entity.User;
import in.ashokit.service.UserService;

@Controller
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/")
	public String login(Model model) {
		model.addAttribute("loginObj", new LoginForm());
		return "index";
	}
	
	@PostMapping("/login")
	public String loginHandler(@ModelAttribute("loginObj") LoginForm login, Model model) {
		
		User loginCheck = userService.loginCheck(login);
		if(loginCheck == null) {
			model.addAttribute("errMsg", "Invalid Credentials");
			return "index";
		}
		
		if(!loginCheck.getPwdUpdated()) {
			ResetPassword reset = new ResetPassword();
			reset.setUserId(loginCheck.getUserId());
			reset.setEmail(loginCheck.getEmail());
			model.addAttribute("resetPwd", reset);
			return "resetPassword";
		}
		
		return "redirect:/dashboard";
	}
	
	@PostMapping("/update")
	public String updatePassword(@ModelAttribute("resetPwd") ResetPassword resetPwd, Model model) {
		boolean updatePassword = userService.updatePassword(resetPwd);
		if(updatePassword) {
			return "redirect:/dashboard";
		}else {
			model.addAttribute("errMsg", "Failed to reset password");
			return "resetPassword";
		}
	}
	
	@GetMapping("/dashboard")
	public String buildDashboard(Model model) {
		String quote = userService.getQuote();
		model.addAttribute("quote", quote);
		return "dashboard";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerObj", new RegistrationForm());
		Map<Integer,String> countries = userService.getCountries();
		model.addAttribute("countries", countries);
		return "register";
	}
	
	@GetMapping("/getStates")
	@ResponseBody
	public Map<Integer, String> getStates(@RequestParam("countryId") Integer countryId){
		Country country = new Country();
		country.setCountryId(countryId);
		return userService.getStates(country);
	}
	
	@GetMapping("/getCities")
	@ResponseBody
	public Map<Integer, String> getCities(@RequestParam("stateId") Integer stateId){
		State state = new State();
		state.setStateId(stateId);
		return userService.getCities(state);
	}
	
	@PostMapping("/register")
	public String registerHandler(@ModelAttribute("registerObj") RegistrationForm register, Model model) {				
		User userByEmail = userService.findUserByEmail(register.getEmail());
		if(userByEmail != null) {
			model.addAttribute("errMsg", "User already exists");
		}else {
			boolean saveUser = userService.saveUser(register);
			if(saveUser) {
				model.addAttribute("succMsg", "Registraion successfully completed");
			}else {
				model.addAttribute("errMsg", "Registraion failed");
			}
		}
		Map<Integer,String> countries = userService.getCountries();
		model.addAttribute("countries", countries);
		return "register";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}
	
}

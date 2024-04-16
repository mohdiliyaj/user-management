package in.ashokit.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.Quote;
import in.ashokit.binding.RegistrationForm;
import in.ashokit.binding.ResetPassword;
import in.ashokit.entity.City;
import in.ashokit.entity.Country;
import in.ashokit.entity.State;
import in.ashokit.entity.User;
import in.ashokit.repo.CityRepo;
import in.ashokit.repo.CountryRepo;
import in.ashokit.repo.StateRepo;
import in.ashokit.repo.UserRepo;
import in.ashokit.utils.EmailUtil;
import in.ashokit.utils.PasswordUtil;

@Service
public class UserService implements IUserService {

	private UserRepo userRepo;
	private PasswordUtil passwordUtil;
	private EmailUtil emailUtil;
	private CountryRepo countryRepo;
	private StateRepo stateRepo;
	private CityRepo cityRepo;

	private List<Quote> quote = null;
	private String apiUrl = "https://type.fit/api/quotes";

	public UserService(UserRepo userRepo, PasswordUtil passwordUtil, EmailUtil emailUtil,
			CountryRepo countryRepo, StateRepo stateRepo, CityRepo cityRepo) {
		this.userRepo = userRepo;
		this.passwordUtil = passwordUtil;
		this.emailUtil = emailUtil;
		this.countryRepo = countryRepo;
		this.stateRepo = stateRepo;
		this.cityRepo = cityRepo;
	}

	@Override
	public User loginCheck(LoginForm loginObj) {
		return userRepo.findByEmailAndPassword(loginObj.getEmail(), loginObj.getPassword());
	}

	@Override
	public boolean updatePassword(ResetPassword resetObj) {
		Optional<User> byId = userRepo.findById(resetObj.getUserId());
		if (byId.isPresent()) {
			User user = byId.get();
			user.setPassword(resetObj.getPassword());
			user.setPwdUpdated(true);
			userRepo.save(user);
			return true;
		}
		return false;
	}

	@Override
	public String getQuote() {
		if (quote == null) {
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> forEntity = template.getForEntity(apiUrl, String.class);
			String body = forEntity.getBody();
			try {
				ObjectMapper mapper = new ObjectMapper();
				Quote[] value = mapper.readValue(body, Quote[].class);
				quote = Arrays.asList(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Random random = new Random();
		int nextInt = random.nextInt(quote.size() - 1);
		return quote.get(nextInt).getText();
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public boolean saveUser(RegistrationForm registerObj) {
		User user = new User();
		try {
			BeanUtils.copyProperties(user, registerObj);
			user.setPassword(passwordUtil.generateRandomPassword());
			user.setPwdUpdated(false);
			/*
			 * Country country = new Country();
			 * country.setCountryId(registerObj.getCountryId()); State state = new State();
			 * state.setStateId(registerObj.getStateId()); City city = new City();
			 * city.setCityId(registerObj.getCityId()); user.setCountryId(country);
			 * user.setStateId(state); user.setCityId(city);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		User save = userRepo.save(user);
		if (save.getUserId() != null) {
			
			String subject = "Account created successfully - AshokIT";
			
			String body = "<div style='font-family: Arial, sans-serif; color: #333;'>"
					+ "<p>Hi " + save.getName() + ",</p>"
					+ "<p>Your account has been created successfully on the AshokIT website. "
					+ "Please use the following temporary password to log in:</p>"
					+ "<p style='font-size: 1.2em; background-color: #f5f5f5; padding: 10px;'> Password : " + save.getPassword()
					+ "</p>" + "<p>Thank you,<br/>AshokIT</p>" + "</div>";
			
			emailUtil.sendRegisterMail(body, subject, save.getEmail());
			return true;
		}
		return false;
	}
	
	@Override
	public Map<Integer, String> getCountries(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<Country> all = countryRepo.findAll();
		all.forEach(e->{
			map.put(e.getCountryId(), e.getCountryName());
		});
		return map;
	}
	
	@Override
	public Map<Integer, String> getStates(Country countryId){
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		List<State> byCountry = stateRepo.findByCountry(countryId);
		byCountry.forEach(e->{
			map.put(e.getStateId(), e.getStateName());
		});
		return map;
	}
	
	@Override
	public Map<Integer, String> getCities(State stateId){
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		List<City> byState = cityRepo.findByState(stateId);
		byState.forEach(e->{
			map.put(e.getCityId(), e.getCityName());
		});
		return map;
	}
	
}

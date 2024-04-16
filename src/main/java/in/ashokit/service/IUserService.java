package in.ashokit.service;

import java.util.Map;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.RegistrationForm;
import in.ashokit.binding.ResetPassword;
import in.ashokit.entity.Country;
import in.ashokit.entity.State;
import in.ashokit.entity.User;

public interface IUserService {
	
	public User loginCheck(LoginForm formObj);
	
	public boolean updatePassword(ResetPassword resetObj);

	public String getQuote();
	
	public User findUserByEmail(String email);
	
	public boolean saveUser(RegistrationForm registerObj);
	
	public Map<Integer, String> getCountries();
	
	public Map<Integer, String> getStates(Country countryId);
	
	public Map<Integer, String> getCities(State stateId);
	
}

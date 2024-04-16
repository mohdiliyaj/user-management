package in.ashokit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.Country;
import in.ashokit.entity.State;

public interface StateRepo extends JpaRepository<State, Integer>{
	
	public List<State> findByCountry(Country countryId);
}

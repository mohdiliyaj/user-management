package in.ashokit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.City;
import in.ashokit.entity.State;


public interface CityRepo extends JpaRepository<City, Integer> {
	
	public List<City> findByState(State stateId);
}

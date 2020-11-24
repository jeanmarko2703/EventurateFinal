package pe.edu.upc.serviceinterface;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import pe.edu.upc.entity.Event;

public interface IEventService {
	
	public void insert (Event eve);
	
	List<Event> list();
	
	List<Event> findBynameEvent(@Param("name")String nameEvent);
	
	List<Event> findBycategory(@Param("name")String namecategory);

	Optional<Event> searchId(int idEvent);
	
	public void delete(int idCity);
	
	List<Event> listCompany(int idUser);
	
	List<Event> findBynameEventCompany(@Param("name")String nameEvent, int idUser);
	
	public List<String[]> report3();

	public List<String[]> reportCompany3(int idUser);
}

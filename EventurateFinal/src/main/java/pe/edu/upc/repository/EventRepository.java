package pe.edu.upc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.upc.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>{
	
	@Query("From Event e Where e.nameEvent like %:name%")
	List<Event> findBynameEvent(@Param("name")String nameEvent);
	
	@Query("From Event e Where e.category.nameCategory like %:name%")
	List<Event> findBycategory(@Param("name")String nameCategory);
	
	@Query("from Event e Where e.user.idUser=?1")
	List<Event> Eventforcompany(int id);
	
	@Query("From Event e Where e.nameEvent =?1 AND e.user.idUser=?2")
	List<Event> findBynameEventCompany(String nameEvent, int id);
	
	@Query( value="SELECT e.name_event, ((e.capacity_event - e.available_event)*100)/e.capacity_event from events e",
			nativeQuery = true )
	public List<String[]> report3();
	
	@Query( value="SELECT e.name_event, ((e.capacity_event - e.available_event)*100)/e.capacity_event from events e where e.id_user = ?1",
			nativeQuery = true )
	public List<String[]> reportCompany3(int id);

}

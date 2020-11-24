package pe.edu.upc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.edu.upc.entity.Reservation;

@Repository
public interface ReservationRepository  extends JpaRepository<Reservation, Integer>{

	@Query( value="SELECT e.name_event, e.capacity_event, sum(e.price_event * r.tickets) from events e join reservations r on  e.id_event = r.id_event group by e.name_event, e.capacity_event "
			+ "ORDER BY sum(e.price_event * r.tickets) DESC",
			nativeQuery = true )
	public List<String[]> report1();
	
	@Query( value="SELECT e.name_event, e.capacity_event, sum(e.price_event * r.tickets) from events e join reservations r on  e.id_event = r.id_event where e.id_user=?1 group by e.name_event, e.capacity_event "
			+ "ORDER BY sum(e.price_event * r.tickets) DESC",
			nativeQuery = true )
	public List<String[]> reportCompany1(int id);
	
	@Query( value="SELECT e.name_event, count(r.id_reservation) from events e join reservations r on  e.id_event = r.id_event group by e.name_event ORDER BY count(r.id_reservation) DESC",
			nativeQuery = true )
	public List<String[]> report2();
	
	@Query( value="SELECT e.name_event, count(r.id_reservation) from events e join reservations r on  e.id_event = r.id_event where e.id_user=?1 group by e.name_event ORDER BY count(r.id_reservation) DESC",
			nativeQuery = true )
	public List<String[]> reportCompany2(int id);
	
	@Query( value="select u.name_user, count(r.id_reservation) from users u JOIN cards c on u.id_user = c.id_user JOIN reservations r on r.id_card = c.id_card group by u.name_user order by count(r.id_reservation) desc limit 3",
			nativeQuery = true )
	public List<String[]> report4();
	
	@Query( value="select c.name_category, count(r.id_reservation) from categories c JOIN events e on c.id_category = e.id_category JOIN reservations r on r.id_event = e.id_event group by c.name_category order by count(r.id_reservation) desc",
			nativeQuery = true )
	public List<String[]> report5();
	
	@Query( value="select u.name_user, sum(r.tickets*e.price_event) from users u JOIN events e on u.id_user = e.id_user JOIN reservations r on r.id_event = e.id_event group by u.name_user order by sum(r.tickets*e.price_event) desc",
			nativeQuery = true )
	public List<String[]> report6();
	
	
	/*@Query("from Reservation r join Card c on r.card.idCard=c.idCard where c.user.idUser = ?%idUser%")
	List<Reservation> list(@Param("idUser")int idUser);*/
	
	@Query("from Reservation r join Card c on r.card.idCard=c.idCard where c.user.idUser = ?1")
	List<Reservation> Reservationforclient(int id);
	
	
	
	
}

package pe.edu.upc.serviceinterface;

import java.util.List;

import pe.edu.upc.entity.Reservation;

public interface IReservationService {

	public void insert(Reservation reser);

	public List<Reservation> list(int idUser);

	// Optional<Reservation> searchId(int idCategory);

	public List<String[]> report1();

	public List<String[]> reportCompany1(int idUser);

	public List<String[]> report2();

	public List<String[]> reportCompany2(int idUser);
	
	public List<String[]> report4();
	
	public List<String[]> report5();
	
	public List<String[]> report6();
}

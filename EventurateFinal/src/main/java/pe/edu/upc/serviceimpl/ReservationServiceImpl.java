package pe.edu.upc.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upc.entity.Reservation;
import pe.edu.upc.repository.ReservationRepository;
import pe.edu.upc.serviceinterface.IReservationService;

@Service
public class ReservationServiceImpl implements IReservationService {

	@Autowired
	private ReservationRepository rR;

	@Override
	public void insert(Reservation reser) {
		rR.save(reser);
	}

	@Override
	public List<String[]> report1() {
		return rR.report1();
	}

	@Override
	public List<Reservation> list(int idUser) {
		// TODO Auto-generated method stub
		return rR.Reservationforclient(idUser);
	}

	@Override
	public List<String[]> reportCompany1(int idUser) {
		return rR.reportCompany1(idUser);
	}

	@Override
	public List<String[]> report2() {
		return rR.report2();
	}

	@Override
	public List<String[]> reportCompany2(int idUser) {
		return rR.reportCompany2(idUser);
	}

	@Override
	public List<String[]> report4() {
		return rR.report4();
	}

	@Override
	public List<String[]> report5() {
		return rR.report5();
	}
	
	@Override
	public List<String[]> report6() {
		return rR.report6();
	}


}

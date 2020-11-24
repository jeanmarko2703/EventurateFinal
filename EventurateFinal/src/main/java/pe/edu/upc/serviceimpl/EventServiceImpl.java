package pe.edu.upc.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.upc.entity.Event;
import pe.edu.upc.repository.EventRepository;
import pe.edu.upc.serviceinterface.IEventService;

@Service
public class EventServiceImpl implements IEventService{
	
	@Autowired
	private EventRepository eR;
	
	@Transactional
	@Override
	public void insert (Event eve) {
		
		eR.save(eve);
	}
	
	@Override
	public List<Event> list() {
		
		return eR.findAll();
	}

	@Override
	public List<Event> findBynameEvent(String nameEvent) {
		// TODO Auto-generated method stub
		return eR.findBynameEvent(nameEvent);
	}

	@Override
	public List<Event> findBycategory(String namecategory) {
		
		return eR.findBycategory(namecategory);
	}

	@Override
	public Optional<Event> searchId(int idEvent) {
		return eR.findById(idEvent);
	}

	@Override
	public void delete(int idCity) {
		eR.deleteById(idCity);	
	}

	@Override
	public List<Event> listCompany(int idUser) {
		return eR.Eventforcompany(idUser);
	}

	@Override
	public List<Event> findBynameEventCompany(String nameEvent, int idUser) {
		return eR.findBynameEventCompany(nameEvent, idUser);
	}

	@Override
	public List<String[]> report3() {
		return eR.report3();
	}

	@Override
	public List<String[]> reportCompany3(int idUser) {
		return eR.reportCompany3(idUser);
	}
	
}

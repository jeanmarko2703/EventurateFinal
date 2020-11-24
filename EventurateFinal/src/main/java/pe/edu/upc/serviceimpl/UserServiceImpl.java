package pe.edu.upc.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upc.entity.Users;
import pe.edu.upc.repository.UserRepository;
import pe.edu.upc.serviceinterface.IUserService;

@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserRepository uR;

	@Override
	public List<Users> findBydniClient(String dniClient) {
		return uR.findBydniClient(dniClient);
	}

	@Override
	public List<Users> findBynameUserCompany(String nameUser) {
		return uR.findBynameUserCompany(nameUser);
	}

	@Override
	public void delete(int idUser) {
		uR.deleteById(idUser);
		
	}

	@Override
	public Optional<Users> searchId(int idUser) {
		return uR.findById(idUser);
	}


	@Override
	public List<Users> listClients() {
		// TODO Auto-generated method stub
		return uR.listClients();
	}


	@Override
	public List<Users> listCompanies() {
		// TODO Auto-generated method stub
		return uR.listCompanies();
	}

	@Override
	public int insertClient(Users user) {
		int rpta = uR.searchDNI(user.getDniClient());
		int rpta2 = uR.searchEmail(user.getEmailUser());
		
		if(rpta == 0) {
			if(rpta2 == 0) {
				uR.save(user);
				return rpta2;
			}else {
				return rpta2;
			}
		}else {
			return rpta;	
		}	
	}

	@Override
	public int insertCompany(Users user) {
		int rpta = uR.searchRUC(user.getRucCompany());
		int rpta2 = uR.searchEmail(user.getEmailUser());
		
		if(rpta == 0) {
			if(rpta2 == 0) {
				uR.save(user);
				return rpta2;
			}else {
				return rpta2;
			}
		}else {
			return rpta;	
		}	
	}

	@Override
	public int updateClient(Users user) {
		Optional<Users>currentClient = uR.findById(user.getIdUser());
		int rpta = uR.searchDNI(user.getDniClient());
		int rpta2 = uR.searchEmail(user.getEmailUser());
		if((rpta==0 && rpta2==0)||
			(rpta2==0 && rpta==1 && currentClient.get().getDniClient().equals(user.getDniClient())) || 
			(rpta==0 && rpta2==1 && currentClient.get().getEmailUser().equals(user.getEmailUser()))||
			(rpta==1 && currentClient.get().getDniClient().equals(user.getDniClient()) &&
			rpta2==1 && currentClient.get().getEmailUser().equals(user.getEmailUser()))) {
			uR.save(user);
		}
		int rpta3=rpta+rpta2;
		return rpta3;
	}

	@Override
	public int updateCompany(Users user) {
		Optional<Users>currentClient = uR.findById(user.getIdUser());
		int rpta = uR.searchRUC(user.getRucCompany());
		int rpta2 = uR.searchEmail(user.getEmailUser());
		if((rpta==0 && rpta2==0)||
			(rpta2==0 && rpta==1 && currentClient.get().getRucCompany().equals(user.getRucCompany())) || 
			(rpta==0 && rpta2==1 && currentClient.get().getEmailUser().equals(user.getEmailUser()))||
			(rpta==1 && currentClient.get().getRucCompany().equals(user.getRucCompany()) &&
			rpta2==1 && currentClient.get().getEmailUser().equals(user.getEmailUser()))) {
			uR.save(user);
		}
		int rpta3=rpta+rpta2;
		return rpta3;
	}

	@Override
	public Users findByEmailUser(String email) {
		// TODO Auto-generated method stub
		return uR.findByEmailUser(email);
	}
	
}

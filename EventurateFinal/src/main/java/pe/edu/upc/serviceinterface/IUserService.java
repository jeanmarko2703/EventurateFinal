package pe.edu.upc.serviceinterface;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.entity.Users;

public interface IUserService {

	public int insertClient(Users user);
	
	public int insertCompany(Users user);

	List<Users> listClients();
	
	List<Users> listCompanies();

	List<Users> findBydniClient(String dniClient);
	
	List<Users> findBynameUserCompany(String nameUser);
	
	public void delete (int idUser);
	
	Optional<Users> searchId(int idUser);
	
	public int updateClient(Users user);
	
	public int updateCompany(Users user);
	
	public Users findByEmailUser(String email);
	
}

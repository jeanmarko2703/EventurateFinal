package pe.edu.upc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.upc.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{

	@Query("select count(u.emailUser) from Users u where u.emailUser=:emailUser or u.dniClient=:dniClient or u.rucCompany=:rucCompany")
	public int searchUser(@Param("emailUser")String emailUser,@Param("dniClient")String dniClient,@Param("rucCompany")String rucCompany);
	
	@Query("from Users u where u.dniClient like %:dniClient% and u.role.nameRole='ROLE_CLIENT'")
	List<Users> findBydniClient(@Param("dniClient") String dniClient);
	
	@Query("from Users u where u.nameUser like %:nameUser% and u.role.nameRole='ROLE_COMPANY'")
	List<Users> findBynameUserCompany(@Param("nameUser") String nameUser);
	
	@Query("from Users u where u.role.nameRole='ROLE_CLIENT'")
	List<Users> listClients();
	
	@Query("from Users u where u.role.nameRole='ROLE_COMPANY'")
	List<Users> listCompanies();
	
	@Query("select count(u.emailUser) from Users u where u.emailUser=:email")
	public int searchEmail(@Param("email")String emailUser);
	
	@Query("select count(u.dniClient) from Users u where u.dniClient=:dni")
	public int searchDNI(@Param("dni")String emailUser);
	
	@Query("select count(u.rucCompany) from Users u where u.rucCompany=:ruc")
	public int searchRUC(@Param("ruc")String emailUser);
	
	@Query("from Users u where u.emailUser like %:email%")
	public Users findByEmailUser(@Param("email") String email);
	
}

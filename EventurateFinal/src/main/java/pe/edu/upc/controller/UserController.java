package pe.edu.upc.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.el.parser.ParseException;

import pe.edu.upc.entity.City;
import pe.edu.upc.entity.Role;
import pe.edu.upc.entity.Users;
import pe.edu.upc.serviceinterface.ICityService;
import pe.edu.upc.serviceinterface.IRoleService;
import pe.edu.upc.serviceinterface.IUserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private IUserService uS;

	@Autowired
	private ICityService cS;

	@Autowired
	private IRoleService rS;

	@GetMapping("/registerClient")
	public String newClient(Model model) {
		model.addAttribute("listaCiudades", cS.list());
		model.addAttribute("userClient", new Users());
		return "registerClient";
	}

	@GetMapping("/registerCompany")
	public String newCompany(Model model) {
		model.addAttribute("listaCiudades", cS.list());
		model.addAttribute("userCompany", new Users());
		return "registerCompany";
	}

	@GetMapping("/listClient")
	public String listClient(Model model) {
		try {
			model.addAttribute("userClient", new Users());
			model.addAttribute("listaClientes", uS.listClients());
		} catch (Exception e) {
			System.out.println("Error al listar clientes en el controller");
		}
		return "user/listUserClient";
	}

	@GetMapping("/listCompany")
	public String listCompany(Model model) {
		try {
			model.addAttribute("userCompany", new Users());
			model.addAttribute("listaEmpresas", uS.listCompanies());
		} catch (Exception e) {
			System.out.println("Error al listar empresas en el controller");
		}
		return "user/listUserCompany";
	}

	@RequestMapping("/irupdateClient/{id}")
	public String irupdateClient(@PathVariable int id, Model model, RedirectAttributes objRedir) {
		Optional<Users> objUser = uS.searchId(id);
		if (objUser == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrió un error");
			return "redirect:/users/listClient";
		} else {
			model.addAttribute("listaCiudades", cS.list());
			model.addAttribute("listaClientes", uS.listClients());
			model.addAttribute("userClient", objUser.get());
			return "user/uuserClient";
		}
	}

	@RequestMapping("/irupdateCompany/{id}")
	public String irupdateCompany(@PathVariable int id, Model model, RedirectAttributes objRedir) {
		Optional<Users> objUser = uS.searchId(id);
		if (objUser == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrió un error");
			return "redirect:/users/listCompany";
		} else {
			model.addAttribute("listaCiudades", cS.list());
			model.addAttribute("listaEmpresas", uS.listCompanies());
			model.addAttribute("userCompany", objUser.get());
			return "user/uuserCompany";
		}
	}

	@RequestMapping("/findClient")
	public String findClient(Model model, @Validated Users user) throws ParseException {
		List<Users> listaClientes;
		model.addAttribute("userClient", new Users());
		listaClientes = uS.findBydniClient(user.getDniClient());
		if (listaClientes.isEmpty()) {
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaClientes", listaClientes);
		return "user/listUserClient";
	}

	@RequestMapping("/findCompany")
	public String findCompany(Model model, @Validated Users user) throws ParseException {
		List<Users> listaEmpresas;
		model.addAttribute("userCompany", new Users());
		listaEmpresas = uS.findBynameUserCompany(user.getNameUser());
		if (listaEmpresas.isEmpty()) {
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaEmpresas", listaEmpresas);
		return "user/listUserCompany";
	}

	@RequestMapping("/deleteClient/{id}")
	public String deleteUserClient(Model model, @PathVariable(value = "id") int id) {
		try {
			if (id > 0) {
				uS.delete(id);
			}
			model.addAttribute("userClient", new Users());
			model.addAttribute("city", new City());
			model.addAttribute("mensaje", "Se eliminó correctamente!");
			model.addAttribute("listaClientes", uS.listClients());
		} catch (Exception e) {
			model.addAttribute("userClient", new Users());
			model.addAttribute("city", new City());
			model.addAttribute("mensaje", "No se puede eliminar!!");
			model.addAttribute("listaClientes", uS.listClients());
		}
		return "user/listUserClient";
	}

	@RequestMapping("/deleteCompany/{id}")
	public String deleteUserCompany(Model model, @PathVariable(value = "id") int id) {
		try {
			if (id > 0) {
				uS.delete(id);
			}
			model.addAttribute("userCompany", new Users());
			model.addAttribute("city", new City());
			model.addAttribute("mensaje", "Se eliminó correctamente!");
			model.addAttribute("listaEmpresas", uS.listCompanies());
		} catch (Exception e) {
			model.addAttribute("userCompany", new Users());
			model.addAttribute("city", new City());
			model.addAttribute("mensaje", "No se puede eliminar!!");
			model.addAttribute("listaEmpresas", uS.listCompanies());
		}
		return "user/listUserCompany";
	}

	@PostMapping("/saveClient")
	public String saveClient(@Valid Users user, BindingResult result, Model model, SessionStatus status)
			throws Exception {
		if (result.hasErrors()) {
			model.addAttribute("userClient", new Users());
			model.addAttribute("listaCiudades", cS.list());
			return "user/userClient";
		} else {
			for (Role r : rS.list()) {
				if (r.getIdRole() == 2) {
					user.setRole(r);
				}
			}
			String password = new BCryptPasswordEncoder().encode(user.getPasswordUser());
			user.setPasswordUser(password);
			int rpta = uS.insertClient(user);
			if (rpta > 0) {
				model.addAttribute("userClient", new Users());
				model.addAttribute("listaCiudades", cS.list());
				model.addAttribute("mensaje", "Este dato ya esta registrado");
				return "/user/userClient";
			} else {
				model.addAttribute("mensaje", "Se guardó correctamente");
				status.setComplete();
			}

		}
		model.addAttribute("listaClientes", uS.listClients());
		return "redirect:/login";

	}

	@PostMapping("/saveCompany")
	public String saveCompany(@Valid Users user, BindingResult result, Model model, SessionStatus status)
			throws Exception {
		if (result.hasErrors()) {
			model.addAttribute("userCompany", new Users());
			model.addAttribute("listaCiudades", cS.list());
			return "user/userCompany";
		} else {

			for (Role r : rS.list()) {
				if (r.getIdRole() == 3) {
					user.setRole(r);
				}
			}
			String password = new BCryptPasswordEncoder().encode(user.getPasswordUser());
			user.setPasswordUser(password);
			int rpta = uS.insertCompany(user);
			if (rpta > 0) {
				model.addAttribute("userCompany", new Users());
				model.addAttribute("listaCiudades", cS.list());
				model.addAttribute("mensaje", "Ya existe el usuario con el mismo Dni y/o ruc y/o email");
				return "/user/userCompany";
			} else {
				model.addAttribute("mensaje", "Se guardó correctamente");
				status.setComplete();
			}

		}
		model.addAttribute("listaEmpresas", uS.listCompanies());
		return "redirect:/login";

	}

	@PostMapping("/updateClient")
	public String updateClient(@Valid Users user, BindingResult result, Model model, SessionStatus status)
			throws Exception {
		if (result.hasErrors()) {
			model.addAttribute("userClient", new Users());
			model.addAttribute("listaCiudades", cS.list());
			return "user/uuserClient";
		} else {
			for (Role r : rS.list()) {
				if (r.getIdRole() == 2) {
					user.setRole(r);
				}
			}
			int rpta=uS.updateClient(user);
			if (rpta > 0) {
				model.addAttribute("userClient", new Users());
				model.addAttribute("listaCiudades", cS.list());
				model.addAttribute("mensaje", "Este dato ya esta registrado");
				return "redirect:/users/listClient";				
			} else {
				this.listClient(model);
				model.addAttribute("mensaje", "Se actualizó el cliente correctamente");
				return "redirect:/users/listClient";
			}			
		}
	}

	@PostMapping("/updateCompany")
	public String udpateCompany(@Valid Users user, BindingResult result, Model model, SessionStatus status)
			throws Exception {
		if (result.hasErrors()) {
			model.addAttribute("userCompany", new Users());
			model.addAttribute("listaCiudades", cS.list());
			return "user/uuserCompany";
		} else {
			for (Role r : rS.list()) {
				if (r.getIdRole() == 3) {
					user.setRole(r);
				}
			}
			int rpta = uS.updateCompany(user);
			if (rpta > 0) {
				model.addAttribute("listaCiudades", cS.list());
				model.addAttribute("mensaje", "Ya existe el usuario con el mismo Dni y/o ruc y/o email");
				return "redirect:/users/listCompany";
			} else {
				this.listCompany(model);
				model.addAttribute("mensaje", "Se actualizó la compañia correctamente");
				return "redirect:/users/listCompany";
			}		
		}			
	}

}

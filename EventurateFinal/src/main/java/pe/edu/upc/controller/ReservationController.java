package pe.edu.upc.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import pe.edu.upc.entity.Users;
import pe.edu.upc.serviceinterface.IReservationService;
import pe.edu.upc.serviceinterface.IUserService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	private IReservationService rS;

	@Autowired
	private IUserService uS;

	@GetMapping("/list")
	public String listCity(Model model, Authentication auth, HttpSession session) {
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		try {
			model.addAttribute("listaReservas", rS.list(usuario.getIdUser()));
		} catch (Exception e) {
			System.out.println("Error al listar las reservas en el controller");
		}
		return "reservation/listReservation";
	}

	@RequestMapping("/reports")
	public String Report() {
		return "reservation/reports";
	}
	
	@RequestMapping("/reportsCompany")
	public String ReportCompany() {
		return "reservation/reportsCompany";
	}

	@RequestMapping("/reporte1")
	public String Reporte(Map<String, Object> model) {
		model.put("listReport", rS.report1());
		return "reservation/report1";
	}
	
	@RequestMapping("/reporteCompany1")
	public String ReporteCompany(Map<String, Object> model, Authentication auth, HttpSession session) {
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		model.put("listReport", rS.reportCompany1(usuario.getIdUser()));
		return "reservation/reportCompany1";
	}
	
	@RequestMapping("/reporte2")
	public String Reporte2(Map<String, Object> model) {
		model.put("listReport", rS.report2());
		return "reservation/report2";
	}
	
	@RequestMapping("/reporteCompany2")
	public String ReporteCompany2(Map<String, Object> model, Authentication auth, HttpSession session) {
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		model.put("listReport", rS.reportCompany2(usuario.getIdUser()));
		return "reservation/reportCompany2";
	}
	
	@RequestMapping("/reporte4")
	public String Reporte4(Map<String, Object> model) {
		model.put("listReport", rS.report4());
		return "reservation/report4";
	}
	
	@RequestMapping("/reporte5")
	public String Reporte5(Map<String, Object> model) {
		model.put("listReport", rS.report5());
		return "reservation/report5";
	}
	
	@RequestMapping("/reporte6")
	public String Reporte6(Map<String, Object> model) {
		model.put("listReport", rS.report6());
		return "reservation/report6";
	}

}

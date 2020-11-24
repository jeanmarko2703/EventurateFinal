package pe.edu.upc.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

import pe.edu.upc.entity.Event;
import pe.edu.upc.entity.Reservation;
import pe.edu.upc.entity.Users;
import pe.edu.upc.serviceinterface.ICardService;
import pe.edu.upc.serviceinterface.ICategoryService;
import pe.edu.upc.serviceinterface.IEventService;
import pe.edu.upc.serviceinterface.IReservationService;
import pe.edu.upc.serviceinterface.IUserService;

@Controller
@RequestMapping("/events")
public class EventController {

	@Autowired
	private IEventService eS;

	@Autowired
	private ICategoryService cS;

	@Autowired
	private ICardService caS;

	@Autowired
	private IReservationService rS;

	@Autowired
	private IUserService uS;

	@RequestMapping("/newReservation/{id}")
	public String irUpdate(@PathVariable int id, Model model, RedirectAttributes objRedir, Authentication auth,
			HttpSession session) {

		Reservation reser = new Reservation();
		Optional<Event> objEvent = eS.searchId(id);

		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);

		if (objEvent == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrió un error");
			return "redirect:/events/listEvClient";
		} else {

			model.addAttribute("listaEventos", eS.list());
			model.addAttribute("listaTarjetas", caS.list(usuario.getIdUser()));
			model.addAttribute("reservation", reser);
			// model.addAttribute("event", objCat.get());
			reser.setEvent(objEvent.get());
			;

			return "reservation/reservation";
		}
	}

	@PostMapping("/saveReservation")
	public String saveReservation(@Valid Reservation reservation, BindingResult result, Model model,
			SessionStatus status, Authentication auth, HttpSession session) throws Exception {
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		if (result.hasErrors()) {
			model.addAttribute("listaEventos", eS.list());
			model.addAttribute("listaTarjetas", caS.list(usuario.getIdUser()));
			return "reservation/reservation";
		} else {

			for (Event e : eS.list()) {
				if (reservation.getEvent().getIdEvent() == e.getIdEvent()) {

					if ((e.getAvailableEvent() != 0) && (e.getAvailableEvent() >= reservation.getTickets())) {
						e.setAvailableEvent(e.getAvailableEvent() - reservation.getTickets());
					} else {
						model.addAttribute("mensaje",
								"Stock de entradas agotadas y/o numero de entradas excede el stock disponible");
						model.addAttribute("listaEventos", eS.list());
						model.addAttribute("listaTarjetas", caS.list(usuario.getIdUser()));
						return "reservation/reservation";
					}

				}
			}

			Date date = new Date();

			long time = date.getTime();

			Timestamp ts = new Timestamp(time);

			reservation.setDateReservation(ts);

			rS.insert(reservation);

		}
		model.addAttribute("listaReservas", rS.list(usuario.getIdUser()));
		return "redirect:/events/listEvClient";
	}

	@GetMapping("/new")
	public String newEvent(Model model) {

		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("event", new Event());
		return "event/event";
	}

	@PostMapping("/save")
	public String saveEvent(@Valid Event event, BindingResult result, Model model, SessionStatus status,
			Authentication auth, HttpSession session) throws Exception {

		if (result.hasErrors()) {
			model.addAttribute("listaCategorias", cS.list());
			return "event/event";
		} else {			
			event.setAvailableEvent(event.getCapacityEvent());
			String useremail = auth.getName();
			Users usuario = (Users) uS.findByEmailUser(useremail);
			event.setUser(usuario);
			eS.insert(event);
			model.addAttribute("listaEventosEmpresa", eS.listCompany(usuario.getIdUser()));
			return "redirect:/events/listEvCompany";
		}		
	}

	@GetMapping("/listEvCompany")
	public String listEventEmpresa(Model model, Authentication auth, HttpSession session) {
		try {
			String useremail = auth.getName();
			Users usuario = (Users) uS.findByEmailUser(useremail);
			model.addAttribute("event", new Event());
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("listaEventosEmpresa", eS.listCompany(usuario.getIdUser()));
		} catch (Exception e) {
			System.out.println("Error al listar eventos de empresa");
		}
		return "event/listEventCompany";
	}

	@GetMapping("/listEvClient")
	public String listEventCliente(Model model) {
		try {
			model.addAttribute("event", new Event());
			model.addAttribute("listaEventos", eS.list());
			model.addAttribute("listaCategorias", cS.list());
		} catch (Exception e) {
			System.out.println("Error al listar eventos de clientes");
		}
		return "event/listEventClient";
	}

	@GetMapping("/list")
	public String listEventAdministrador(Model model) {
		try {
			model.addAttribute("event", new Event());
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("listaEventos", eS.list());
		} catch (Exception e) {
			System.out.println("Error al listar eventos de administrador");
		}
		return "event/listEvent";
	}

	@RequestMapping("/findEvCompany")
	public String findEvent(Model model, @Validated Event event, Authentication auth, HttpSession session) throws ParseException {
		
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		List<Event> listaEventos;
		listaEventos = eS.findBynameEventCompany(event.getNameEvent(),usuario.getIdUser());
		if (listaEventos.isEmpty()) {
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("mensaje", "No se encontro");
		}
		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("listaEventosEmpresa", listaEventos);
		return "event/listEventCompany";
	}

	@RequestMapping("/findEvClient")
	public String findEventClient(Model model, @Validated Event event) throws ParseException {
		List<Event> listaEventos;
		listaEventos = eS.findBynameEvent(event.getNameEvent());
		if (listaEventos.isEmpty()) {
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("listaEventos", listaEventos);
		return "event/listEventClient";
	}

	@RequestMapping("/find")
	public String findEventCompany(Model model, @Validated Event event) throws ParseException {
		List<Event> listaEventos;
		listaEventos = eS.findBynameEvent(event.getNameEvent());
		if (listaEventos.isEmpty()) {
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("listaEventos", listaEventos);
		return "event/listEvent";
	}

	@RequestMapping("/findCategoryClient")
	public String findNameCategoryClient(Model model, @Validated Event event) throws ParseException {
		List<Event> listaEventos;
		listaEventos = eS.findBycategory(event.getCategory().getNameCategory());
		if (listaEventos.isEmpty()) {
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("listaEventos", listaEventos);
		return "event/listEventClient";
	}

	@RequestMapping("/findCategoryCompany")
	public String findNameCategoryCompany(Model model, @Validated Event event) throws ParseException {
		List<Event> listaEventos;
		listaEventos = eS.findBycategory(event.getCategory().getNameCategory());
		if (listaEventos.isEmpty()) {
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("listaEventos", listaEventos);
		return "event/listEventCompany";
	}

	@RequestMapping("/findCategory")
	public String findNameCategory(Model model, @Validated Event event) throws ParseException {
		List<Event> listaEventos;
		listaEventos = eS.findBycategory(event.getCategory().getNameCategory());
		if (listaEventos.isEmpty()) {
			model.addAttribute("listaCategorias", cS.list());
			model.addAttribute("mensaje", "No se encontró");
		}
		model.addAttribute("listaCategorias", cS.list());
		model.addAttribute("listaEventos", listaEventos);
		return "event/listEvent";
	}

	@RequestMapping("/delete/{id}")
	public String deleteEvent(Model model, @PathVariable(value = "id") int id) {
		try {
			if (id > 0) {
				for (Event e : eS.list()) {
					if (e.getIdEvent() == id) {
						if (e.getAvailableEvent() == e.getCapacityEvent()) {
							cS.delete(id);
						}
					}
				}

			}
		} catch (Exception e) {

		}
		return "event/listEventCompany";
	}
	
	@RequestMapping("/reporte3")
	public String Reporte2(Map<String, Object> model) {
		model.put("listReport", eS.report3());
		return "event/report3";
	}
	
	@RequestMapping("/reporteCompany3")
	public String ReporteCompany2(Map<String, Object> model, Authentication auth, HttpSession session) {
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		model.put("listReport", eS.reportCompany3(usuario.getIdUser()));
		return "event/reportCompany3";
	}

}

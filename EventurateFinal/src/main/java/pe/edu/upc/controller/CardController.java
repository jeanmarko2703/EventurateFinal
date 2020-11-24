package pe.edu.upc.controller;

import java.util.List;
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

import pe.edu.upc.entity.Card;
import pe.edu.upc.entity.Users;
import pe.edu.upc.serviceinterface.IBanking_entityService;
import pe.edu.upc.serviceinterface.ICardService;
import pe.edu.upc.serviceinterface.IUserService;

@Controller
@RequestMapping("/cards")
public class CardController {

	@Autowired
	private ICardService cS;

	@Autowired
	private IBanking_entityService bS;

	@Autowired
	private IUserService uS;

	@GetMapping("/new")
	public String newCard(Model model) {
		model.addAttribute("listaEntidadesBancarias", bS.list());
		model.addAttribute("card", new Card());
		return "card/card";
	}

	@PostMapping("/save")
	public String saveCard(@Valid Card card, BindingResult result, Model model, SessionStatus status,
			Authentication auth, HttpSession session) throws Exception {
		if (result.hasErrors()) {
			model.addAttribute("listaEntidadesBancarias", bS.list());
			return "card/card";
		} else {
			String useremail = auth.getName();
			Users usuario = (Users) uS.findByEmailUser(useremail);
			card.setUser(usuario);
			int rpta = cS.insert(card);
			if (rpta > 0) {
				model.addAttribute("listaEntidadesBancarias", bS.list());
				model.addAttribute("mensaje", "La tarjeta ya existe!!");
				return "card/card";
			} else {
				model.addAttribute("listaEntidadesBancarias", bS.list());
				model.addAttribute("listaTarjetas", cS.list(usuario.getIdUser()));
				return "redirect:/cards/list";
			}

		}

	}

	@GetMapping("/list")
	public String listCard(Model model, Authentication auth, HttpSession session) {

		try {
			String useremail = auth.getName();
			Users usuario = (Users) uS.findByEmailUser(useremail);

			model.addAttribute("card", new Card());
			model.addAttribute("listaTarjetas", cS.list(usuario.getIdUser()));
		} catch (Exception e) {
			System.out.println("Error al listar tarjetas en el controller");
		}
		return "card/listCardClient";
	}

	@RequestMapping("/find")
	public String findCard(Model model, @Validated Card card) throws ParseException {

		List<Card> listaTarjetas;
		listaTarjetas = cS.findBynameCard(card.getCardHolder());
		if (listaTarjetas.isEmpty()) {
			model.addAttribute("mensaje", "No se encontro");
		}
		model.addAttribute("listaTarjetas", listaTarjetas);
		return "card/listCardClient";
	}

	@RequestMapping("/delete/{id}")
	public String deleteCard(Model model, @PathVariable(value = "id") int id, Authentication auth,
			HttpSession session) {
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		try {
			if (id > 0) {
				cS.delete(id);
			}
			model.addAttribute("card", new Card());
			model.addAttribute("mensaje", "Se elimino correctamente");

			model.addAttribute("listaTarjetas", cS.list(usuario.getIdUser()));
		} catch (Exception e) {
			model.addAttribute("card", new Card());
			model.addAttribute("mensaje", "Error al eliminar en el controller");
			model.addAttribute("listaTarjetas", cS.list(usuario.getIdUser()));
		}
		return "card/listCardClient";
	}

	@RequestMapping("/irupdate/{id}")
	public String irUpdate(@PathVariable int id, Model model, RedirectAttributes objRedir) {
		Optional<Card> objCar = cS.searchId(id);
		if (objCar == null) {
			objRedir.addFlashAttribute("mensaje", "Ocurrio un error");
			return "redirect:/cards/list";
		} else {
			model.addAttribute("listaEntidadesBancarias", bS.list());
			model.addAttribute("card", objCar.get());
			return "card/ucard";
		}
	}

	@PostMapping("/update")
	public String updateCard(@Valid Card card, BindingResult result, Model model, SessionStatus status,
			Authentication auth,HttpSession session)
			throws Exception {
		
		String useremail = auth.getName();
		Users usuario = (Users) uS.findByEmailUser(useremail);
		if (result.hasErrors()) {
			return "card/card";
		} else {
			card.setUser(usuario);
			cS.update(card);
			this.listCard(model, auth, session);
			model.addAttribute("mensaje", "Se actualizó la tarjeta correctamente");
		}
		model.addAttribute("listaTarjetas", cS.list(usuario.getIdUser()));
		return "card/listCardClient";
	}

}

package pe.edu.upc.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.upc.entity.Card;
import pe.edu.upc.repository.CardRepository;
import pe.edu.upc.serviceinterface.ICardService;

@Service
public class CardServiceImpl implements ICardService{

	@Autowired
	private CardRepository cR;

	@Override
	public int insert(Card car) {
		int rpta = cR.searchCard(car.getCardNumber());
		if (rpta == 0) {
			cR.save(car);
		}
		return rpta;
	}

	@Override
	public List<Card> findBynameCard(String cardHolder) {
		// TODO Auto-generated method stub
		return cR.findBynameCard(cardHolder);
	}

	@Override
	public void delete(int idCard) {
		cR.deleteById(idCard);
		
	}

	@Override
	public Optional<Card> searchId(int idCard) {
		return cR.findById(idCard);
	}

	@Override
	public int update(Card car) {
		Optional<Card> currentCard = cR.findById(car.getIdCard());
		int rpta = cR.searchCard(car.getCardNumber());
		if (rpta == 0 || (rpta == 1 && currentCard.get().getCardNumber().equals(car.getCardNumber()))) {
			cR.save(car);
		}
		return rpta;
	}

	@Override
	public List<Card> list(int idUser) {
		
		return cR.list(idUser);
	}

}

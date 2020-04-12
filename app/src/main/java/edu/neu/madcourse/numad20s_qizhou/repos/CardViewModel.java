package edu.neu.madcourse.numad20s_qizhou.repos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.neu.madcourse.numad20s_qizhou.model.Card;
import edu.neu.madcourse.numad20s_qizhou.model.FamilyMember;

public class CardViewModel extends AndroidViewModel {

    private CardRepository cardRepository;

    private LiveData<List<Card>> allCards;

    public CardViewModel (Application application) {
        super(application);
        cardRepository = new CardRepository(application);
        allCards = cardRepository.getAllCards();
    }

    LiveData<List<Card>> getAllCards() { return allCards; }

    public void insert(Card card) { cardRepository.insert(card); }

    public void deleteAll() {
        cardRepository.deleteAll();
    }

    public void deleteCardById(Integer id) {
        cardRepository.deleteCardById(id);
    }

    public Card getCardById(Integer id) throws ExecutionException, InterruptedException {
        return cardRepository.getCardById(id);
    }

    public Card getCardByTaskId(Integer id) throws ExecutionException, InterruptedException {
        return cardRepository.getCardByTaskId(id);
    }

    public void updateCard(Card card) {
        cardRepository.updateCard(card);
    }
}

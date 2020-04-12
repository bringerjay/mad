package edu.neu.madcourse.numad20s_qizhou.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.neu.madcourse.numad20s_qizhou.model.Card;

public class CardRepository {

    private CardDAO cardDAO;
    private LiveData<List<Card>> allCards;

    CardRepository(Application application) {
        KanBanDatabase db = KanBanDatabase.getDatabase(application);
        cardDAO = db.cardDAO();
        allCards = cardDAO.getAllCards();
    }

    LiveData<List<Card>> getAllCards() {
        return allCards;
    }

    void insert(Card card) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            cardDAO.insert(card);
        });
    }

    public void deleteAll() {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            cardDAO.deleteAll();
        });
    }


    public Card getCardById(Integer id) throws ExecutionException, InterruptedException {

        Callable<Card> callable = new Callable<Card>() {
            @Override
            public Card call() throws Exception {
                return cardDAO.getCardById(id);
            }
        };
        Future<Card> future = Executors.newSingleThreadExecutor().submit(callable);
        Card card = null;
        try{ card = future.get();} catch (ExecutionException e) {
            e.printStackTrace();
            throw new ExecutionException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException();
        }
        return card;
    }

    public void deleteCardById(Integer id) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            cardDAO.deleteById(id);
        });
    }

    public void updateCard(Card card) {
        KanBanDatabase.databaseWriteExecutor.execute(() -> {
            cardDAO.update(card);
        });
    }

    public Card getCardByTaskId(Integer id) throws ExecutionException, InterruptedException {

        Callable<Card> callable = new Callable<Card>() {
            @Override
            public Card call() throws Exception {
                return cardDAO.getCardByTaskId(id);
            }
        };
        Future<Card> future = Executors.newSingleThreadExecutor().submit(callable);
        Card card = null;
        try{ card = future.get();} catch (ExecutionException e) {
            e.printStackTrace();
            throw new ExecutionException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new InterruptedException();
        }
        return card;
    }
}

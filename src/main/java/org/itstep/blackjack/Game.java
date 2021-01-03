package org.itstep.blackjack;

import lombok.extern.slf4j.Slf4j;
import org.itstep.blackjack.card.Card;
import org.itstep.blackjack.event.GameEventListener;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Game {
    public static final int TWENTY_ONE = 21;
    public static final int THRESHOLD = 15;

    private final Player player;
    private final Player dealer;
    private final Deck deck;
    
    private final List<GameEventListener> eventListeners;

    public void addEventListener(GameEventListener listener){
        eventListeners.add(listener);
    }

    public void publishStart(){
        for(GameEventListener l: eventListeners){
            l.gameStart();
        }
    }

    public void publishSand(){
        eventListeners.forEach(GameEventListener::stand);
    }

    public void publishPlayerTakeCard(Card card){
        for (GameEventListener l: eventListeners){
            l.playerGetCard(card, player.getPoints());
        }
    }

    public void publishDealerTakeCard(Card card){
        for (GameEventListener l: eventListeners){
            l.dealerGetCard(card, dealer.getPoints());
        }
    }

    public void publishGameOver(String winner){
        for (GameEventListener l: eventListeners){
            l.gameOver(winner, player.getPoints(), dealer.getPoints());
        }
    }

    public Game() {
        deck = new Deck();
        dealer = new Player(0);
        player = new Player(1000);
        eventListeners = new ArrayList<>();
    }

    public void hit() {
        Card card = deck.getOne();
        player.takeCard(card);
        log.info("Player take a card {}", card);
        publishPlayerTakeCard(card);
        if (player.getPoints() > TWENTY_ONE) {
            dealer.getCards().get(0).setHide(false);

            publishGameOver(getWinner());
            log.info("Game over. Win {}", getWinner());
        }
    }

    public void stand() {
        dealer.getCards().get(0).setHide(false);

        publishSand();
        log.info("Stand");
        while (dealer.getPoints() < THRESHOLD) {
            Card card = deck.getOne();
            dealer.takeCard(card);

            publishDealerTakeCard(card);
            log.info("Dealer take a card {}", card);
        }

        publishGameOver(getWinner());
        log.info("Game over. Win {}", getWinner());
    }

    public void setBet(int amount) throws NoMoneyEnough {
        player.setBet(amount);
        // TODO: реализовать логику добавления ставки
    }

    public String getWinner() {
        if (player.getPoints() <= TWENTY_ONE) {
            if (dealer.getPoints() > TWENTY_ONE) {
                return "Player";
            } else if (player.getPoints() > dealer.getPoints()) {
                return "Player";
            } else {
                return "Dealer";
            }
        }
        return "Dealer";
    }

    public void play() {
        deck.shuffle();
        player.clear();
        dealer.clear();

        publishStart();
        log.info("Play game");

        Card firstCard = deck.getOne();
        player.takeCard(firstCard);

        publishPlayerTakeCard(firstCard);
        log.info("Player take first card {}", firstCard);

        Card second = deck.getOne();
        player.takeCard(second);

        publishPlayerTakeCard(second);
        log.info("Player take second card {}", second);

        Card hiddenCard = deck.getOne();
        hiddenCard.setHide(true);
        dealer.takeCard(hiddenCard);

        publishDealerTakeCard(hiddenCard);
        log.info("Dealer take hidden card {}", hiddenCard);

        Card lastCard = deck.getOne();
        dealer.takeCard(lastCard);

        publishDealerTakeCard(lastCard);
        log.info("Dealer take second card {}", lastCard);
    }

}

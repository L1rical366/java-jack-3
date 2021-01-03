package org.itstep.blackjack.event;

import org.itstep.blackjack.card.Card;

public interface GameEventListener {
    void gameStart();
    void stand();
    void playerGetCard(Card card, int points);
    void dealerGetCard(Card card, int points);
    void gameOver(String winner, int playerPoints, int dealerPoints);
    void playerSetBet(int amount);
}

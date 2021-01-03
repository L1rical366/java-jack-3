package org.itstep.blackjack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import org.itstep.blackjack.card.Card;
import org.itstep.blackjack.card.Rank;
import org.itstep.blackjack.card.Suite;
import org.itstep.blackjack.ui.CardView;

public class BlackjackController {

    @FXML
    private ImageView cardImageView;

    private CardView[] cards;
    private int idx;

    public BlackjackController() {
        cards = new CardView[Suite.values().length * Rank.values().length];
        int i=0;
        for(Suite suite: Suite.values()) {
            for (Rank rank: Rank.values()) {
                cards[i++] = new CardView(new Card(rank, suite));
            }
        }
    }

    @FXML
    public void initialize() {
        cardImageView.setImage(cards[idx].getImage());
    }

    @FXML
    public void nextCardAction(ActionEvent actionEvent) {
        cardImageView.setImage(cards[++idx].getImage());
    }
}

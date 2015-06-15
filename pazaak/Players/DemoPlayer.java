package Players;

import java.util.Collection;

import Mechanics.*;

public class DemoPlayer extends Player {

	public DemoPlayer() {
		name = "Your Name Here";
	}

	public void getResponse(int wins[], boolean isPlayerOne,
			Collection<Card> yourHand, Collection<Card> opponentHand,
			Collection<Card> yourSideDeck, int opponentSideDeckCount,
			Action opponentAction, boolean opponentDidPlay) {
		action = null;
		cardToPlay = null;
	}

}

package fjmp.game.numeritoletrita;

import fjmp.game.framework.Screen;
import fjmp.game.framework.impl.AndroidGame;

public class GameTest extends AndroidGame {
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}

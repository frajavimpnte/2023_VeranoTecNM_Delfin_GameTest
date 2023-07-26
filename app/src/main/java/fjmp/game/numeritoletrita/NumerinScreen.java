package fjmp.game.numeritoletrita;

import java.util.List;

import fjmp.game.framework.Game;
import fjmp.game.framework.Graphics;
import fjmp.game.framework.Input;
import fjmp.game.framework.Screen;

public class NumerinScreen extends Screen {
    // time variable
    double time = 0.0;

    // movimiento de nummerin
    int posxNumerin = -80;
    int numberSelected = -1;
    double animateTime =0.0;
    public NumerinScreen(Game game) {
        super(game);
    }

    // board game numbers
    int xPosBoard[] = {160, 160, 420, 420, 420, 720, 720, 720, 960, 960 };
    int yPosBoard[] = {440, 180,  60, 280, 490,  60, 280, 490, 440, 180};

    @Override
    public void update(float deltaTime) {
        // animación o movimiento =========================================
        posxNumerin = -80 +
                (int) (100 * Math.sin(2.0 * Math.PI * time / 10.0));
        time += deltaTime;

        // eventos =======================================================
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();


        // back and home, sound events
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {

                //back
                if (inBounds(event, 140, 10, 101, 101)) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                } else if  (inBounds(event, 1110, 230, 150, 180)) { // home
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                }
                // number selected sound
                for (int j = 0; j < 10; j++) {
                    if (inBounds(event, xPosBoard[j], yPosBoard[j], 100, 100)) {
                        Assets.soundNumbers[j].play(1);
                        numberSelected = j;
                        break;
                    }
                }
            }
        }
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width -1 &&
                event.y > y && event.y < y + height -1)
            return true;

        return false;
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background,0,0);
        g.drawPixmap(Assets.back, 140,10);
        g.drawPixmap(Assets.home, 1110, 230);
        g.drawPixmap(Assets.play, 1060, 530);
        if (Settings.soundEnabled)
            g.drawPixmap(Assets.sound, 1040,10);
        else
            g.drawPixmap(Assets.silence, 1040, 10);


        // draw board game
        for (int i = 0; i < xPosBoard.length; i++) {

            g.drawPixmapScale(Assets.bubble_numerin, xPosBoard[i], yPosBoard[i], 1.5f);
            if (numberSelected != i)
                g.drawPixmap(Assets.numeros_row, xPosBoard[i], yPosBoard[i],
                        i*100, 0, 100, 100);
            else { // animate
                animateTime += deltaTime;
                g.drawPixmapScale(Assets.numeros_row, xPosBoard[i], yPosBoard[i],
                        i*100, 0, 100, 100,
                        (float) (1.0+  1.5* Math.sin(2*Math.PI*animateTime/1.0)));
                if (animateTime >=1.0f) {
                    numberSelected = -1;
                    animateTime = 0.0f;
                }
            }
        }

        g.drawPixmap(Assets.numerin, posxNumerin, 510);
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
        Assets.music.pause();
    }
    @Override
    public void resume() {
        if (Settings.soundEnabled)
            Assets.music.play();
    }
    @Override
    public void dispose(){
    }
}

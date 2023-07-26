package fjmp.game.numeritoletrita;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import fjmp.game.framework.Game;
import fjmp.game.framework.Graphics;
import fjmp.game.framework.Input.TouchEvent;
import fjmp.game.framework.Screen;
import fjmp.game.framework.gl.Camera2D;
import fjmp.game.framework.gl.SpriteBatcher;
import fjmp.game.framework.impl.GLScreen;
import fjmp.game.framework.math.Rectangle;
import fjmp.game.framework.math.Vector2;

import android.util.Log;


public class MainMenuScreen extends Screen {

    Rectangle soundBounds;

    double time = 0.0;

    int posxNumerin = 350;
    int posyLetrita = 180;
    int posxPlayNumerin = 135 +
            (int)(150.0f*Math.sin(Math.PI + 2.0*Math.PI*time/10.0));
    int posyPlayNumerin = 460 +
            (int)(150.0f*Math.cos(Math.PI + 2.0*Math.PI*time/10.0));

    int posxPlayLetrita = 1000 +
            (int)(150.0f*Math.sin(-Math.PI/2 + 2.0*Math.PI*time/10.0));
    int posyPlayLetrita = 460 +
            (int)(150.0f*Math.cos(-Math.PI/2 + 2.0*Math.PI*time/10.0));

    public MainMenuScreen(Game game) {
        super(game);
    }
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        // move letrita and numerin
        posxNumerin = 350 + (int)(50*Math.sin(2.0*Math.PI*time/10.0));
        posyLetrita = 180 + (int)(50*Math.sin(2.0*Math.PI*time/20.0));
        // move play letrita
        posxPlayNumerin = 135 +
                (int)(150.0f*Math.sin(Math.PI + 2.0*Math.PI*time/10.0));
        posyPlayNumerin = 460 +
                (int)(150.0f*Math.cos(Math.PI + 2.0*Math.PI*time/10.0));

        posxPlayLetrita = 1000 +
                (int)(150.0f*Math.sin(-Math.PI/2 + 2.0*Math.PI*time/30.0));
        posyPlayLetrita = 460 +
                (int)(150.0f*Math.cos(-Math.PI/2 + 2.0*Math.PI*time/30.0));

        time+=deltaTime;
        // events
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len=touchEvents.size();

        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);

            if(event.type == TouchEvent.TOUCH_UP) {
                Log.d("MainMenuScreen: ",
                        "touch at: " + event.x + ", " + event.y );
                // sound event test
                if (inBounds(event, 1040, 10, 101,101)) {
                    Assets.click.play(1);
                    if (Settings.soundEnabled)
                        Assets.music.pause();
                    else
                        Assets.music.play();
                    Settings.soundEnabled = !Settings.soundEnabled;
                }
                // play numerin
                if(inBounds(event, 80, 340, 300,295)) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);

                    game.setScreen(new NumerinScreen(game));

                    return;
                }

            }
        }

    }
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width -1 &&
            event.y > y && event.y < y + height -1)
            return true;

        return false;
    }
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.background,0,0);
        g.drawPixmap(Assets.logo, 360, 10);
        if (Settings.soundEnabled)
            g.drawPixmap(Assets.sound, 1040,10);
        else
            g.drawPixmap(Assets.silence, 1040, 10);

        g.drawPixmap(Assets.planetaNumerin, 80, 340);
        g.drawPixmap(Assets.play, posxPlayNumerin, posyPlayNumerin);
        g.drawPixmap(Assets.numerin, posxNumerin, 430);


        g.drawPixmap(Assets.planetaLetrita, 920, 340);
        g.drawPixmap(Assets.play, posxPlayLetrita, posyPlayLetrita);
        g.drawPixmap(Assets.letrita, 1110, posyLetrita);


    }
    public void pause() {
        Settings.save(game.getFileIO());
        Assets.music.pause();
    }
    public void resume() {
        if (Settings.soundEnabled)
            Assets.music.play();
    }

    public void dispose(){
    }
}

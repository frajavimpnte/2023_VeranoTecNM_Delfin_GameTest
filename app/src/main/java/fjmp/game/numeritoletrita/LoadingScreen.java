package fjmp.game.numeritoletrita;

import android.graphics.PixelFormat;

import fjmp.game.framework.Game;
import fjmp.game.framework.Graphics;
import fjmp.game.framework.Screen;
import fjmp.game.framework.Graphics.PixmapFormat;
import fjmp.game.framework.Sound;
import fjmp.game.framework.impl.GLScreen;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        // Loading Assets for all screen =============================
        Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);

        Assets.sound = g.newPixmap("sonido2.png", PixmapFormat.ARGB4444);
        Assets.silence = g.newPixmap("silencio2.png", PixmapFormat.ARGB4444);

        Assets.click=game.getAudio().newSound("click.ogg");
        // Setting files
        Settings.load(game.getFileIO());

        // Soundtrack
        Assets.music = game.getAudio().newMusic("EduGamePlatTest6.wav");
        Assets.music.setLooping(true);
        Assets.music.setVolume(0.5f);
        if (Settings.soundEnabled)
            Assets.music.play();


        // Loading Assets for MainMenuScreen ===============================
        Assets.logo=g.newPixmap("logo.png", PixmapFormat.ARGB4444);
        Assets.letrita=g.newPixmap("letrita.png", PixmapFormat.ARGB4444);
        Assets.numerin=g.newPixmap("numerin.png", PixmapFormat.ARGB4444);
        Assets.planetaLetrita=g.newPixmap("planeta_letrita.png", PixmapFormat.ARGB4444);
        Assets.planetaNumerin=g.newPixmap("planeta_numerin.png", PixmapFormat.ARGB4444);
        Assets.play=g.newPixmap("play2.png", PixmapFormat.ARGB4444);

        // Loading Assets for NumerinScree
        Assets.home=g.newPixmap("home2.png", PixmapFormat.ARGB4444);
        Assets.back=g.newPixmap("regresar2.png", PixmapFormat.ARGB4444);
        Assets.bubble_numerin=g.newPixmap("bubble_numerin.png", PixmapFormat.ARGB4444);
        Assets.numeros_row=g.newPixmap("numeros_row.png", PixmapFormat.ARGB4444);

        //String names[] = {"Numeros"}
        Assets.soundNumbers = new Sound[10];
        for (int i = 0; i < 10; i++) {
            Assets.soundNumbers[i] =
                    game.getAudio().newSound("Numeros-" + i + ".wav");
        }
        // Asset for NumerinMemoScren
        Assets.cardNumerinMemoSprit = g.newPixmap("card_numerin_sprit.png",PixmapFormat.ARGB4444);
        // new screen main menu ============================================
        game.setScreen(new MainMenuScreen(game));
        //game.setScreen(new NumerinScreen(game));
        //game.setScreen(new NumerinMemoScreen(game));
    }
    public void present(float deltaTime){

    }
    public void pause() {

    }
    public void resume() {

    }
    public void dispose() {

    }
}

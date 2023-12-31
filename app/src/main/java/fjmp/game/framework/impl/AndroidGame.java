package fjmp.game.framework.impl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.Toast;
//import android.widget.TextView;

import fjmp.game.framework.Audio;
import fjmp.game.framework.FileIO;
import fjmp.game.framework.Game;
import fjmp.game.framework.Graphics;
import fjmp.game.framework.Input;
import fjmp.game.framework.Screen;


public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;
    WakeLock wakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        Bitmap fb=null;
        AndroidGraphics ag = new AndroidGraphics(getAssets(), fb);
        Pixmap ap = ag.newPixmap("pausemenu.png", Graphics.PixmapFormat.ARGB4444);
        */

        boolean isLandScape=getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth=isLandScape?1280:720;
        int frameBufferHeight=isLandScape?720:1280;
        Bitmap frameBuffer=Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight,Config.RGB_565);

        float scaleX=(float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY=(float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        renderView=new AndroidFastRenderView(this, frameBuffer);
        graphics=new AndroidGraphics(getAssets(),frameBuffer);
        fileIO=new AndroidFileIO(this);
        audio=new AndroidAudio(this);
        input= new AndroidInput(this,renderView,scaleX,scaleY);
        screen=getStartScreen();
        setContentView(renderView);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"GLGame");
    }
    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }
    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    public Input getInput() {
        return input;
    }
    public FileIO getFileIO() {
        return fileIO;
    }

    public Graphics getGraphics(){
        return graphics;
    }

    public Audio getAudio(){
        return audio;
    }

    public void setScreen(Screen screen) {
        if (screen==null)
            throw  new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen=screen;

    }
    public Screen getCurrentScreen() {
        return screen;
    }
}

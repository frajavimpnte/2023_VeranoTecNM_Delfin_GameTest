package fjmp.game.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;
    Bitmap framebuffer;
    Thread renderThread=null;
    SurfaceHolder holder;
    Rect dstRect;
    volatile boolean running=false;

    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game=game;
        this.framebuffer=framebuffer;
        this.holder=getHolder();
        dstRect=new Rect();
    }

    public void resume() {
        running=true;
        renderThread=new Thread(this);
        renderThread.start();
    }

    public void run() {

        long startTime=System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;

            float deltaTime=(System.nanoTime()-startTime)/1000000000.0f;
            startTime=System.nanoTime();

            //first time is loading screen,
            //second time it's MainMenuScreen
            //then its user dependant
            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        running=false;
        while(true){
            try{
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                //retry
            }
        }
    }
}

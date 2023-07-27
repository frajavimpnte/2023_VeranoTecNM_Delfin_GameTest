package fjmp.game.numeritoletrita;

import fjmp.game.framework.DynamicGameObject;
import fjmp.game.framework.Game;
import fjmp.game.framework.Graphics;
import fjmp.game.framework.Input;
import fjmp.game.framework.Pixmap;
import fjmp.game.framework.Screen;

import android.util.Log;

import java.util.List;

public class NumerinMemoScreen extends Screen {
    // time variable
    double time = 0.0;

    // movimiento de nummerin
    int posyNumerin = 250;

    // Board with animation sprints
    final int BOARD_ROWS = 4;
    final int BOARD_COLS = 5;
    CardMemoSprit boardMemo[][] = new CardMemoSprit[BOARD_ROWS][BOARD_COLS];


    public NumerinMemoScreen(Game game) {
        super(game);

        // asignar memporia al board e inicializar numeros
        // 0,1,2,3,4
        // 5,6,7,8,9
        // 0,1,2,3,4
        // 5,6,7,8,9
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                boardMemo[i][j] = new CardMemoSprit(
                        0.1f,
                        false,
                        ((i%2)*5 + j));
            }
        }
    }
    @Override
    public void update(float deltaTime) {
        // animación o movimiento =========================================
        posyNumerin = 250 +
                (int) (200 * Math.sin(2.0 * Math.PI * time / 20.0));
        time += deltaTime;

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                if (boardMemo[i][j].isAnimating()) {
                    //Log.d("Update: ",
                    //       "i=" + i + ", j=" + j + "--------------");
                    //Log.d("Update: ",
                    //        "animTime="+  boardMemo[i][j].getAnimTime());
                    boardMemo[i][j].update(deltaTime);
                }
            }
        }


        // evento sobre carta===============================================
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();

        // back and home, sound events
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                // number selected sound
                for (int ii = 0; ii < BOARD_ROWS; ii++) {
                    int y = 120 + ii * (100 + 40);
                    for (int jj = 0; jj < BOARD_COLS; jj++) {
                        int x = 200 + jj * (100 + 60);

                        if (inBounds(event, x, y,
                                100, 100)) {
                            boardMemo[ii][jj].setAnimatingFlag(true);
                            Assets.soundNumbers[boardMemo[ii][jj].getNumber()].play(1);
                            //Assets.click.play(1);
                            //Log.d("TouchEvent: ",
                            //        "ii=" + ii + " jj="+ jj + "--------------------");
                        }
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

        for (int i = 0; i < BOARD_ROWS; i++) {
            int y = 120 + i * (100 + 40);
            for (int j = 0; j < BOARD_COLS; j++) {
               int  x = 200 +  j*(100 + 60);
                //animateCard(x, y, deltaTime, 0.2f);
                //int keyFrame = boardMemo[i][j].getKeyFrame();
                /*
                int keyFrame = 0;
                if (boardMemo[i][j].isAnimating()) {
                    keyFrame = boardMemo[i][j].getKeyFrame();
                    Log.d("Present: ",
                            "i=" + i + " j="+ j +
                                    ", keyframe:" + boardMemo[i][j].getKeyFrame() +
                                    ", animTimne:" +  boardMemo[i][j].getAnimTime() +
                                    ", is animating--------------------");

                }*/
                g.drawPixmap(Assets.cardNumerinMemoSprit, x, y,
                        boardMemo[i][j].getSpritFrame()*100,
                        0, 100, 100 );
                // dibujar si se termino la anumacion
                if (boardMemo[i][j].getKeyFrame() >= boardMemo[i][j].sequenseCardAnim.length-1) {
                    g.drawPixmap(Assets.numeros_row, x, y,
                            100*boardMemo[i][j].getNumber(),
                            0, 100, 100 );


                }
            }
        }

        g.drawPixmap(Assets.numerin, 10, posyNumerin);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    static class MemoGame {

    }
    static class CardMemoSprit {
        boolean animatingFlag = false;
        float animTime = 0;                             // runnig animation time
        float frameDuration = 0.1f;                        // frame change time
        int frameNumber;                                // actual frame number
        int[] sequenseCardAnim = {0,1,2,3,4,3,2,1,0};   // animation sequence
        int number = 0;                                 // state number

        public CardMemoSprit(float frameDuration, boolean animatingFlag) {
            this.frameDuration = frameDuration;
            this.animatingFlag = animatingFlag;
        }
        public CardMemoSprit(float frameDuration, boolean animatingFlag, int number) {
            this.frameDuration = frameDuration;
            this.animatingFlag = animatingFlag;
            this.number = number;
        }

        public void seNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return this.number;
        }
        public void setAnimatingFlag(boolean animatingFlag) {
            if (animatingFlag)
                animTime = 0.0f;
            this.animatingFlag = animatingFlag;
        }
        public void update(float deltaTime) {
            if (animatingFlag)
                animTime += deltaTime;
        }
        public float getAnimTime() {
            return this.animTime;
        }
        public boolean isAnimating() {
            return this.animatingFlag;
        }

        public int getSpritFrame() {
            getKeyFrame();
            return sequenseCardAnim[frameNumber];
        }
        public int getKeyFrame() {
            frameNumber = (int) (animTime / frameDuration);
            //Log.d("getKeyFrame", "fn1:" + frameNumber + "---------------");
            frameNumber = Math.min(sequenseCardAnim.length-1, frameNumber);
            //Log.d("getKeyFrame", "fn2:" + frameNumber + "---------------");
            if (frameNumber == sequenseCardAnim.length -1)
                animatingFlag = false;
            return frameNumber;
        }

    }
}

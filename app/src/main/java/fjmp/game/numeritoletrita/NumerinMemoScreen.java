package fjmp.game.numeritoletrita;

import fjmp.game.framework.DynamicGameObject;
import fjmp.game.framework.Game;
import fjmp.game.framework.Graphics;
import fjmp.game.framework.Input;
import fjmp.game.framework.Pixmap;
import fjmp.game.framework.Screen;

import android.util.Log;

import java.util.List;
import java.util.Random;

public class NumerinMemoScreen extends Screen {
    // time variable
    double time = 0.0;

    // movimiento de nummerin
    int posyNumerin = 250;

    // Board with animation sprints
    MemoGame memo;
    public NumerinMemoScreen(Game game) {
        super(game);
        memo = new MemoGame();
    }
    @Override
    public void update(float deltaTime) {
        // animación o movimiento =========================================
        posyNumerin = 250 +
                (int) (200 * Math.sin(2.0 * Math.PI * time / 20.0));
        time += deltaTime;

        // update memo
        memo.update(deltaTime);

        // evento sobre carta===============================================
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();

        // back and home, sound events
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                // number selected sound
                for (int ii = 0; ii < memo.BOARD_ROWS; ii++) {
                    for (int jj = 0; jj < memo.BOARD_COLS; jj++) {
                        if (inBounds(event,
                                memo.getCardPositionX(ii, jj),
                                memo.getCardPositionY(ii, jj),
                                100, 100)) {
                            memo.setAnimationFlag(ii, jj,true);
                            Assets.soundNumbers[memo.getCardNumber(ii, jj)].play(1);
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

        for (int i = 0; i < memo.BOARD_ROWS; i++) {
            for (int j = 0; j < memo.BOARD_COLS; j++) {
                g.drawPixmap(Assets.cardNumerinMemoSprit,
                        memo.getCardPositionX(i,j),
                        memo.getCardPositionY(i, j),
                        memo.getCardSpritFrame(i,j)*100,
                        0, 100, 100 );
                // dibujar si se termino la animacion
                if (memo.getDrawCardNumber(i, j))
                    g.drawPixmap(Assets.numeros_row,
                            memo.getCardPositionX(i,j),
                            memo.getCardPositionY(i,j),
                            100*memo.getCardNumber(i, j),
                            0, 100, 100 );

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
        final int BOARD_ROWS = 4;
        final int BOARD_COLS = 5;
        private CardMemoSprit boardMemo[][] = new CardMemoSprit[BOARD_ROWS][BOARD_COLS];

        public MemoGame() {
            // asignar memporia al board e inicializar numeros
            // 0,1,2,3,4
            // 5,6,7,8,9
            // 0,1,2,3,4
            // 5,6,7,8,9

            for (int i = 0; i < BOARD_ROWS; i++) {
                int y = 120 + i * (100 + 40);
                for (int j = 0; j < BOARD_COLS; j++) {
                    int  x = 200 +  j*(100 + 60);
                    boardMemo[i][j] = new CardMemoSprit(
                            0.1f,
                            false,
                            ((i%2)*5 + j));
                    boardMemo[i][j].setPosition(x,y);
                }
            }

            // reorganizar
            shuffle(10);
        }
        private void shuffle(int amount) {
            Random numAleatorioRows = new Random();
            Random numAleatorioCols = new Random();

            // Constructor pasadole una semilla
            for (int i = 0; i < amount; i++) {
                int x1 = numAleatorioRows.nextInt(BOARD_ROWS);
                int y1 = numAleatorioCols.nextInt(BOARD_COLS);

                int x2 = numAleatorioRows.nextInt(BOARD_ROWS);
                int y2 = numAleatorioCols.nextInt(BOARD_COLS);

                int numberCard1 = boardMemo[x1][y1].getNumber();
                boardMemo[x1][y1].setNumber( boardMemo[x2][y2].getNumber()  );
                boardMemo[x2][y2].setNumber(numberCard1);
            }
        }
        public int getCardSpritFrame(int row, int col) {
            return boardMemo[row][col].getSpritFrame();
        }
        public int getCardNumber(int row, int col) {
            return boardMemo[row][col].getNumber();
        }
        public int getCardPositionX(int row, int col) {
            return boardMemo[row][col].getX();
        }
        public int getCardPositionY(int row, int col) {
            return boardMemo[row][col].getY();
        }
        public void setAnimationFlag(int row, int col, boolean flag) {
            boardMemo[row][col].setAnimatingFlag(flag);
        }
        public boolean getDrawCardNumber(int row, int col) {
            return boardMemo[row][col].getDrawNumber();
        }
        public void update(float deltaTime) {
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
        }
    }
    static class CardMemoSprit {
        private boolean animatingFlag;                  // animante flag
        private float animTime;                             // runnig animation time
        private float frameDuration = 0.1f;                     // frame change time
        private int frameNumber;                                // actual frame number
        private int[] sequenseCardAnim = {0,1,2,3,4,3,2,1,0};   // animation sequence
        private int number = 0;                                 // card number, default 0
        private boolean drawNumber = false;                     // draw nummber, default false
        private int x;                                          // card position x
        private int y;                                          // card position x

        public CardMemoSprit(float frameDuration, boolean animatingFlag) {
            this.frameDuration = frameDuration;
            this.animatingFlag = animatingFlag;
        }
        public CardMemoSprit(float frameDuration, boolean animatingFlag, int number) {
            this.frameDuration = frameDuration;
            this.animatingFlag = animatingFlag;
            this.number = number;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return this.x;
        }
        public int getY() {
            return this.y;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return this.number;
        }

        public boolean getDrawNumber() {
            return this.drawNumber;
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
            if (frameNumber == sequenseCardAnim.length -1) {
                animatingFlag = false;
                drawNumber = true;
            }
            return frameNumber;
        }
    }
}

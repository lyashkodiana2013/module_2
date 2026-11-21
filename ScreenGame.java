package io.github.Di.screens;

import static io.github.Di.MyGdxGame.SCR_HEIGHT;
import static io.github.Di.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;

import io.github.Di.characters.Bird;
import io.github.Di.components.MovingBackground;
import io.github.Di.MyGdxGame;
import io.github.Di.components.PointCounter;
import io.github.Di.characters.Tube;

public class ScreenGame implements Screen {

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    MyGdxGame myGdxGame;

    Bird bird;
    PointCounter pointCounter;
    MovingBackground background;
    Texture heartTexture;
    Texture exit;
    int exitX = 1200;
    int exitY = 650;

    int tubeCount = 3;
    Tube[] tubes;

    int gamePoints;
    boolean isGameOver;

    public int lives = 3;
    float Timer = 0;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        initTubes();
        background = new MovingBackground("background/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 4, 250, 200);
        heartTexture = new Texture("heart.png");
        exit = new Texture("exit.png");
        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);
    }


    @Override
    public void show() {
        gamePoints = 0;
        lives = 3;
        isGameOver = false;
        bird.setY(SCR_HEIGHT / 2);
        initTubes();
    }

    @Override
    public void render(float delta) {
        Timer -= delta;

        if (isGameOver) {
            myGdxGame.screenRestart.gamePoints = gamePoints;
            myGdxGame.setScreen(myGdxGame.screenRestart);
            return;
        }

        if (Gdx.input.justTouched()) {
            bird.onClick();

            Vector3 touch = myGdxGame.camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );

            if (touch.x >= exitX && touch.x <= exitX + exit.getWidth() && touch.y >= exitY && touch.y <= exitY + exit.getHeight()) {
                myGdxGame.setScreen(myGdxGame.screenMenu);
            }
        }

        background.move();
        bird.fly();
        if (!bird.isInField() && Timer <= 0) {
            System.out.println("not in field");
            lives--;
            Timer = 1.5f;
            System.out.println("out of field, lives = " + lives);
            if (lives <= 0) {
                isGameOver = true;
            }
        }
        for (Tube tube : tubes) {
            tube.move();
            if ( Timer <= 0 && tube.isHit(bird)) {
                lives--;
                Timer = 1.5f;
                System.out.println("hit, lives = " + lives);
                if (lives <= 0) {
                    isGameOver = true;
                }
            } else if (tube.needAddPoint(bird)) {
                gamePoints += 1;
                tube.setPointReceived();
                System.out.println(gamePoints);
            }
        }

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        bird.draw(myGdxGame.batch);
        for (Tube tube : tubes) tube.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, gamePoints);
        myGdxGame.batch.draw(exit, SCR_WIDTH - 80, SCR_HEIGHT - 50, 80, 50);
        for (int i = 0; i < lives; i++) {
            myGdxGame.batch.draw(heartTexture, 20 + i * 60, SCR_HEIGHT - 70, 50, 50);
        }

        myGdxGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bird.dispose();
        background.dispose();
        pointCounter.dispose();
        for (int i = 0; i < tubeCount; i++) {
            tubes[i].dispose();
        }
    }

    void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }

}



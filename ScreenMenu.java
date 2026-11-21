package io.github.Di.screens;

import static io.github.Di.MyGdxGame.SCR_HEIGHT;
import static io.github.Di.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.Di.components.MovingBackground;
import io.github.Di.MyGdxGame;
import io.github.Di.components.PointCounter;
import io.github.Di.components.TextButton;

public class ScreenMenu implements Screen {

    MyGdxGame myGdxGame;

    MovingBackground background;
    TextButton buttonRestart;
    PointCounter pointCounter;
    Texture exit;
    int exitX = 1200;
    int exitY = 650;


    int gamePoints;

    public ScreenMenu(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        pointCounter = new PointCounter(750, 530);
        buttonRestart = new TextButton(100, 400, "Start");
        exit = new Texture("exit.png");
        background = new MovingBackground("background/restart_bg.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) {

            Vector3 touch = myGdxGame.camera.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );

            if (buttonRestart.isHit((int) touch.x, (int) touch.y)) {
                myGdxGame.setScreen(myGdxGame.screenGame);
            }

            if (touch.x >= exitX && touch.x <= exitX + exit.getWidth() && touch.y >= exitY && touch.y <= exitY + exit.getHeight()) {
                Gdx.app.exit();
            }

        }

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();
        myGdxGame.batch.draw(exit, exitX, exitY);

        background.draw(myGdxGame.batch);
        buttonRestart.draw(myGdxGame.batch);
        myGdxGame.batch.draw(exit, SCR_WIDTH - 80, SCR_HEIGHT - 50, 80, 50);

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
        background.dispose();
        buttonRestart.dispose();
    }
}

package ua.praetorians.claw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Mammma on 09.06.2017.
 */

public class Logo implements Screen {
    final Claw game;
    private OrthographicCamera camera;
    private final int WIDTH=1366,HEIGHT=768;
    private Texture back,backLogo;
    private double distance=0;
    private Music logoMusic;
    private int x=0;
    private Scroll scroll;
    private boolean first = true;
    private long startTime;


    float stateTime=0;


    public Logo(final Claw game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        back = game.manager.get("background-logo.png", Texture.class);
        backLogo = game.manager.get("background.png", Texture.class);
        logoMusic = game.manager.get("Music/Level1.mp3", Music.class);
        logoMusic.setVolume(.5f);


//        logoMusic.setPosition(10);
        while (!game.manager.update()) {
        }

        game.manager.load("47403.png", Texture.class);
        game.manager.load("clawLogo.png", Texture.class);
        game.manager.load("mainMenu.png", Texture.class);
        game.manager.load("quitMenu.png", Texture.class);
        game.manager.load("singleMenu.png", Texture.class);
        game.manager.load("star.png", Texture.class);


        game.manager.load("clawSprite.png", Texture.class);
        game.manager.load("Music/MENUBED.mp3", Music.class);
        game.manager.load("Music/SELECT.mp3", Sound.class);
        game.manager.load("Music/INTRO.mp3", Music.class);
        game.manager.load("Music/LEFTFOOT.mp3", Music.class);
        game.manager.load("Music/RIGHTFOOT1.mp3", Sound.class);

        scroll = new Scroll(game, backLogo);
        scroll.setScroll(true);
        scroll.setOpen(false);
        startTime = TimeUtils.millis();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stateTime += Gdx.graphics.getDeltaTime();
        game.batch.setProjectionMatrix(camera.combined);
        if(Gdx.input.isTouched()){
            Claw.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(Claw.touchPos);
        }else{
            Claw.touchPos.set(0,0,0);
        }
        game.batch.begin();
        game.batch.draw(backLogo,0,0);
        game.batch.draw(back,WIDTH/2-back.getWidth()/2,0);
        scroll.scrolling();
        game.batch.end();


        if(game.manager.update()&& TimeUtils.millis()-startTime>5000){
            scroll.setScroll(true);
            if(!scroll.isOpen()) {
                dispose();
                game.setScreen(new MainMenu(game));
            }
        }

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
        logoMusic.stop();
        logoMusic.dispose();
        back.dispose();
        //backLogo.dispose();
        game.manager.unload("background-logo.png");
    }
}

package ua.praetorians.claw;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
// Mammma
public class Claw extends Game implements ApplicationListener {
	public SpriteBatch batch;
	public AssetManager manager;
	static Vector3 touchPos;
	static float stateTime=0;
	static String[] credits;
	public Scroll scroll;
	public BitmapFont font;
	public Texture skullLogo,backLogo;
	public OrthographicCamera camera;
	 final int WIDTH=1366,HEIGHT=768;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();
		manager = new AssetManager();
		font = new BitmapFont();
		font.getData().scale(1.4f);
		touchPos = new Vector3();
		FileHandle fileCredits = Gdx.files.internal("data/CREDITS.txt");
		credits = fileCredits.readString().split("\n");
		manager.load("background.png",Texture.class);
		manager.load("background-logo.png",Texture.class);
		manager.load("Music/Level1.mp3", Music.class);
		manager.load("Music/rollUp.mp3", Sound.class);
		manager.load("scroll.png",Texture.class);
		manager.load("skullLogo.png", Texture.class);
		while (!manager.update()){}
		skullLogo = manager.get("skullLogo.png",Texture.class);
		backLogo = manager.get("background.png", Texture.class);
		scroll = new Scroll(this, manager.get("background.png", Texture.class));
		this.setScreen(new Logo(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

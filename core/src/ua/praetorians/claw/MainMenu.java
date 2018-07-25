package ua.praetorians.claw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Mammma on 09.09.2017.
 */

public class MainMenu implements Screen {

    final Claw game;

    //private OrthographicCamera camera;

    private Texture back,skullLogo,clawLogo,mainMenu,quitMenu,singleMenu,playerTexture;
    private Music menuMusic,intro;
    private Scroll scroll;
    private long startTime;
    float difTime=MathUtils.random(0,4000),runTime = MathUtils.random(2000,10000);
    // stateTime=0;
    Button btnNewGame,btnMain,btnMulti,btnOption,btnCredits,btnQiute;
    Button btnExitSure,btnExitYes,btnExitNo;
    Button btnSingle,btnNew,btnLoad,btnBack;
    static Sound select;
    private boolean isMusic=false;
    Star star;
    ArrayList<Rectangle> starsRec;
    ArrayList<Star> stars;
    Rectangle clowLogoRec;
    Player player;
    ArrayList<Button> buttons;

    public MainMenu(final Claw game){
        this.game = game;
        game.camera = new OrthographicCamera();
        game.camera.setToOrtho(false,game.WIDTH,game.HEIGHT);
        back = game.manager.get("background.png",Texture.class);
        skullLogo = game.manager.get("skullLogo.png",Texture.class);
        clawLogo = game.manager.get("clawLogo.png",Texture.class);
        clowLogoRec = new Rectangle(game.WIDTH/2-clawLogo.getWidth()/2,game.HEIGHT-clawLogo.getHeight(),clawLogo.getWidth(),clawLogo.getHeight());
        playerTexture = game.manager.get("47403.png",Texture.class);
        mainMenu = game.manager.get("mainMenu.png",Texture.class);
        quitMenu = game.manager.get("quitMenu.png",Texture.class);
        singleMenu = game.manager.get("singleMenu.png",Texture.class);
        menuMusic = game.manager.get("Music/MENUBED.mp3",Music.class);
        select =  game.manager.get("Music/SELECT.mp3",Sound.class);
        intro =  game.manager.get("Music/INTRO.mp3",Music.class);


        starsRec = new ArrayList<Rectangle>();
        addStarRec(197,21,30,30);
        addStarRec(186,102,30,30);
        addStarRec(21,179,30,30);
        addStarRec(512,196,30,30);
        addStarRec(457,21,30,30);
        addStarRec(189,169,30,30);
        addStarRec(257,47,30,30);
        addStarRec(197,21,30,30);
        addStarRec(401,47,30,30);
        addStarRec(435,30,30,30);
        addStarRec(197,21,30,30);
        addStarRec(375,168,30,30);

        stars = new ArrayList<Star>();
        stars.add(new Star(starsRec.get(MathUtils.random(0,starsRec.size()-1)),game));
        stars.add(new Star(starsRec.get(MathUtils.random(0,starsRec.size()-1)),game));
        stars.add(new Star(starsRec.get(MathUtils.random(0,starsRec.size()-1)),game));
        stars.add(new Star(starsRec.get(MathUtils.random(0,starsRec.size()-1)),game));
        stars.add(new Star(starsRec.get(MathUtils.random(0,starsRec.size()-1)),game));

        intro.play();
        intro.setVolume(.5f);
        scroll = new Scroll(game, back);
        scroll.setScroll(true);
        scroll.setOpen(false);
        startTime = TimeUtils.millis();

        buttons = new ArrayList<Button>();
        createBtn();
        player = new Player(-200,(float)btnMain.getY()+(float)btnMain.getHeight(),game.manager.get("clawSprite.png",Texture.class),game,currentPlayer());
        player.size = 1.7f;

        System.out.println(player.getX()+" "+player.getY()+" "+player.getHp()+" "+player.getLive());

    }


    public String currentPlayer(){
        String name="";
        String[] list;
        FileHandle listPl = Gdx.files.internal("data/listPlayers.txt");
        list = listPl.readString().split("\n");
        for(String s : list){
            if(Integer.parseInt(s.substring(0,1))==1){
                name = s.substring(1,s.length()-1);
            }
        }
        System.out.println(name);
        return name;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
        Claw.stateTime = Gdx.graphics.getDeltaTime();
        game.batch.setProjectionMatrix(game.camera.combined);

        if(!intro.isPlaying()&&!isMusic){
            menuMusic.play();
            menuMusic.setLooping(true);
            menuMusic.setVolume(.5f);
            isMusic=true;
        }

        if(Gdx.input.justTouched()){
            Claw.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(Claw.touchPos);
        }else{
            Claw.touchPos.set(0,0,0);
        }
        game.batch.begin();
        game.batch.draw(back,0,0,game.WIDTH,game.HEIGHT);
        game.batch.draw(skullLogo,game.WIDTH/2-skullLogo.getWidth()/2,0);
        game.batch.draw(clawLogo,clowLogoRec.getX(),clowLogoRec.getY(),clowLogoRec.getWidth(),clowLogoRec.getHeight());
        //game.batch.draw(mainMenu,game.WIDTH/2-mainMenu.getWidth()/2,game.HEIGHT-clawLogo.getHeight()-mainMenu.getHeight());
//        btnMain.draw(game);
        for(Button b : buttons){
            b.draw(game);
            if(b.isShow()){b.action();}
        }
//        btnNewGame.draw(game);
//        if(btnNewGame.isShow()){btnNewGame.action();}
//        btnMulti.draw(game);
//        if(btnMulti.isShow()){btnMulti.action();}
//        btnOption.draw(game);
//        if(btnOption.isShow()){ btnOption.action();}
//        btnCredits.draw(game);
//        if(btnCredits.isShow()){btnCredits.action();}
////        btnQiute.draw(game);
////        if(btnQiute.isShow()){btnQiute.action();}
//        btnExitSure.draw(game);
//        btnExitYes.draw(game);
//        if(btnExitYes.isShow()){btnExitYes.action();}
//        btnExitNo.draw(game);
//        if(btnExitNo.isShow()){btnExitNo.action();}
//        //----SINGLE
//        btnSingle.draw(game);
//        btnNew.draw(game);
//        if(btnNew.isShow()){btnNew.action();}
//        btnLoad.draw(game);
//        if(btnLoad.isShow()){btnLoad.action();}
//        btnBack.draw(game);
//        if(btnBack.isShow()){ btnBack.action();}


        for(Star x :stars) {
            x.draw(Claw.stateTime);
            if (x.isFinish()) {
                x.setRectangle(starsRec.get(MathUtils.random(0,starsRec.size()-1)));
                x.setFinish(false);
            }
        }
        autoRunClaw();
        //player.control();
        player.draw();
        scroll.scrolling();
        game.batch.end();

    }

    public void autoRunClaw(){
        player.fall=false;
        player.stay();
        if(TimeUtils.millis()-startTime>difTime){

            if(player.x <-200){
                player.right = true;
                startTime = TimeUtils.millis();
                difTime = MathUtils.random(3000,10000);
            }
            if(player.x>game.WIDTH+200){
                player.right = false;
                startTime = TimeUtils.millis();
                difTime = MathUtils.random(3000,10000);
            }
            if((player.x>=game.WIDTH/6&&player.x<=game.WIDTH*5/6)&&TimeUtils.millis()-startTime>runTime){
                startTime = TimeUtils.millis();
                difTime = MathUtils.random(3000,12000);
                runTime = MathUtils.random(6000,15000);
                player.right = MathUtils.randomBoolean();
            }
            player.run();

        }
    }


    public void addStarRec(float x, float y,float width,float height){
        starsRec.add(new Rectangle(x+clowLogoRec.getX()-width/2,clowLogoRec.getY()+clowLogoRec.getHeight()-y-height/2,width,height));
    }

    public void createBtn(){
        //----------------------MENU-----------------
        btnMain = new Button(new TextureRegion(mainMenu,0,0,285,48)) {
            @Override
            public void action() {
            }
        };
        btnMain.setSize(1.5f);
        btnMain.setX(game.WIDTH/2-btnMain.getWidth()/2+10);
        btnMain.setY(game.HEIGHT/2+btnMain.getHeight()/4);
        buttons.add(btnMain);

        btnNewGame = new Button(game.WIDTH/2,game.HEIGHT/2,285,35,new TextureRegion(mainMenu,0,60,285,35),new TextureRegion(mainMenu,295,60,285,35)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch < 200&&!scroll.isScroll()) {
                    setMenuShow(false);
                    setSingleShow(true);
                }
            }
        };
        btnNewGame.setSize(1.5f);
        btnNewGame.setX(btnNewGame.getX()-btnNewGame.getWidth()/2+10);
        btnNewGame.setY(btnMain.getY()-btnNewGame.getHeight()-20);
        buttons.add(btnNewGame);

        btnMulti = new Button(new TextureRegion(mainMenu,10,100,265,35),new TextureRegion(mainMenu,303,100,265,35)) {
            @Override
            public void action() {
               super.action();
            }
        };
        btnMulti.setSize(1.5f);
        btnMulti.setX(game.WIDTH/2-btnMulti.getWidth()/2+10);
        btnMulti.setY(btnNewGame.getY()-btnMulti.getHeight()-20);
        buttons.add(btnMulti);

        btnOption = new Button(new TextureRegion(mainMenu,60,140,165,35),new TextureRegion(mainMenu,350,140,165,35)) {
            @Override
            public void action() {
                super.action();
            }
        };
        btnOption.setSize(1.5f);
        btnOption.setX(game.WIDTH/2-btnOption.getWidth()/2+10);
        btnOption.setY(btnMulti.getY()-btnOption.getHeight()-20);
        buttons.add(btnOption);

        btnCredits = new Button(new TextureRegion(mainMenu,60,175,165,35),new TextureRegion(mainMenu,350,175,165,35)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch < 200&&!scroll.isScroll()) {
                    scroll.setScroll(true);

                }
                ///    warning !!!!!!!!!!!!!!!!!!
                ///    warning !!!!!!!!!!!!!!!!!!
                ///    warning !!!!!!!!!!!!!!!!!!
                if(!scroll.isOpen()&&!scroll.isWork()&&timeTouch!=0) {
                    game.setScreen(new Credits(game));
                }
            }
        };
        btnCredits.setSize(1.5f);
        btnCredits.setX(game.WIDTH/2-btnCredits.getWidth()/2+10);
        btnCredits.setY(btnOption.getY()-btnCredits.getHeight()-20);
        buttons.add(btnCredits);

        btnQiute = new Button(new TextureRegion(mainMenu,96,245,87,40),new TextureRegion(mainMenu,387,245,87,40)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch < 200&&!scroll.isScroll()) {
                    setMenuShow(false);
                    setExitShow(true);
                }
            }
        };
        btnQiute.setSize(1.5f);
        btnQiute.setX(game.WIDTH/2-btnQiute.getWidth()/2+10);
        btnQiute.setY(btnCredits.getY()-btnQiute.getHeight()-20);
        buttons.add(btnQiute);

        // ---------- EXIT _____________
        btnExitSure = new Button(new TextureRegion(quitMenu,0,0,310,70)) {
            @Override
            public void action() {
            }
        };
        btnExitSure.setShow(false);
        btnExitSure.setSize(1.5f);
        btnExitSure.setX(game.WIDTH/2-btnExitSure.getWidth()/2+10);
        btnExitSure.setY(btnMain.getY()-btnExitSure.getHeight());
        buttons.add(btnExitSure);

        btnExitYes = new Button(new TextureRegion(quitMenu,80,70,80,36),new TextureRegion(quitMenu,0,70,80,36)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch > 100&& timeTouch!=0) {
                    dispose();
                    System.exit(-1);
                }
            }
        };
        btnExitYes.setShow(false);
        btnExitYes.setSize(1.5f);
        btnExitYes.setX(game.WIDTH/2-btnExitYes.getWidth()/2+10);
        btnExitYes.setY(btnExitSure.getY()-btnExitYes.getHeight()-20);
        buttons.add(btnExitYes);

        btnExitNo = new Button(new TextureRegion(quitMenu,0,108,80,36),new TextureRegion(quitMenu,80,108,80,36)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch < 200&&!scroll.isScroll()) {
                    setMenuShow(true);
                    setExitShow(false);
                }
            }
        };
        btnExitNo.setShow(false);
        btnExitNo.setSize(1.5f);
        btnExitNo.setX(game.WIDTH/2-btnExitNo.getWidth()/2+10);
        btnExitNo.setY(btnExitYes.getY()-btnExitNo.getHeight()-20);
        buttons.add(btnExitNo);

        //-------------SINGLE PLAYER--------------
        btnSingle = new Button(new TextureRegion(singleMenu,0,0,368,50)) {
            @Override
            public void action() {
            }
        };
        btnSingle.setShow(false);
        btnSingle.setSize(1.5f);
        btnSingle.setX(game.WIDTH/2-btnSingle.getWidth()/2+10);
        btnSingle.setY(btnMain.getY());
        buttons.add(btnSingle);

        btnNew = new Button(new TextureRegion(singleMenu,70,50,220,38),new TextureRegion(singleMenu,72,160,220,38)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch < 200&&!scroll.isScroll()) {
                    scroll.setScroll(true);

                }
                ///    warning !!!!!!!!!!!!!!!!!!
                ///    warning !!!!!!!!!!!!!!!!!!
                ///    warning !!!!!!!!!!!!!!!!!!
                if(!scroll.isOpen()&&!scroll.isWork()&&timeTouch!=0) {
                    menuMusic.stop();
                    menuMusic.dispose();
                    game.setScreen(new SingleGame(game,"level1",player));
                }
            }
        };
        btnNew.setShow(false);
        btnNew.setSize(1.5f);
        btnNew.setX(game.WIDTH/2-btnNew.getWidth()/2+10);
        btnNew.setY(btnSingle.getY()-btnNew.getHeight()-20);
        buttons.add(btnNew);

        btnLoad = new Button(new TextureRegion(singleMenu,70,86,220,38),new TextureRegion(singleMenu,70,197,225,38)) {
        };
        btnLoad.setShow(false);
        btnLoad.setSize(1.5f);
        btnLoad.setX(game.WIDTH/2-btnLoad.getWidth()/2+10);
        btnLoad.setY(btnNew.getY()-btnLoad.getHeight()-20);
        buttons.add(btnLoad);

        btnBack = new Button(new TextureRegion(singleMenu,118,128,112,35),new TextureRegion(singleMenu,119,238,111,33)) {
            @Override
            public void action() {
                super.action();
                if(!isTouch()&& TimeUtils.millis() - timeTouch < 200&&!scroll.isScroll()) {
                    setMenuShow(true);
                    setSingleShow(false);
                }
            }
        };
        btnBack.setShow(false);
        btnBack.setSize(1.5f);
        btnBack.setX(game.WIDTH/2-btnBack.getWidth()/2+10);
        btnBack.setY(btnLoad.getY()-btnBack.getHeight()-20);
        buttons.add(btnBack);
    }



    public void setMenuShow(boolean x){
        btnQiute.setShow(x);
        btnCredits.setShow(x);
        btnMulti.setShow(x);
        btnMain.setShow(x);
        btnNewGame.setShow(x);
        btnOption.setShow(x);
    }
    public void setExitShow(boolean x){
        btnExitSure.setShow(x);
        btnExitYes.setShow(x);
        btnExitNo.setShow(x);
    }

    public void setSingleShow(boolean x){
        btnSingle.setShow(x);
        btnNew.setShow(x);
        btnLoad.setShow(x);
        btnBack.setShow(x);
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
        menuMusic.dispose();
        back.dispose();
        skullLogo.dispose();
        mainMenu.dispose();
        quitMenu.dispose();
        singleMenu.dispose();
        playerTexture.dispose();

    }
}

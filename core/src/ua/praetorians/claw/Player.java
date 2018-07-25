package ua.praetorians.claw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Mammma on 17.09.2017.
 */

public class Player extends Human implements Action {

    TextureAnim runAnim,stayAnim,jumpAnim,fallAnim,ladderAnim,ladderAnimUp,edgeAnim;
    Music leftRunM,rightRunM;
    boolean changeFoot=false,firstFall=true,ladderUp=false;
    float difTimeStep=400;
    long timeStep=0;
    private int hp;
    private int live;
    private int bullet;
    private int magic;
    private int tnt;
    private int scores;
    private int lvl;
    private String playerName;

    public Player(float x, float y, Texture texture, final Claw game, String playerName) {
        super(x, y, texture,game);
        stayAnim = new TextureAnim();
        stayAnim.anima(new TextureRegion(super.texture,0,118,608,118),8,1,0.1f);
        jumpAnim = new TextureAnim();
        jumpAnim.anima(new TextureRegion(super.texture,0,236,700,118),7,1,0.1f);
        fallAnim = new TextureAnim();
        fallAnim.anima(new TextureRegion(super.texture,0,354,455,118),5,1,0.1f);
        ladderAnim = new TextureAnim();
        ladderAnim.anima(new TextureRegion(super.texture,0,472,900,140),12,1,0.06f);
        ladderAnimUp = new TextureAnim();
        ladderAnimUp.anima(new TextureRegion(super.texture,0,612,450,120),6,1,0.03f);
        edgeAnim = new TextureAnim();
        edgeAnim.anima(new TextureRegion(super.texture,0,732,1000,110),8,1,0.1f);
        runAnim = new TextureAnim();
        runAnim.anima(new TextureRegion(super.texture,0,0,super.texture.getWidth(),118),10,1,0.1f);
        leftRunM = game.manager.get("Music/LEFTFOOT.mp3",Music.class);
        rightRunM = game.manager.get("Music/LEFTFOOT.mp3",Music.class);
        this.playerName =playerName;
        getDataPlayer();
    }


    public void getDataPlayer(){
        FileHandle playerData = Gdx.files.internal("data/"+playerName+".txt");
        String[] stringsPl = playerData.readString().split(";");

        hp = Integer.parseInt(stringsPl[0]);
        live = Integer.parseInt(stringsPl[1]);
        bullet = Integer.parseInt(stringsPl[2]);
        magic = Integer.parseInt(stringsPl[3]);
        tnt = Integer.parseInt(stringsPl[4]);
        scores = Integer.parseInt(stringsPl[5]);
        lvl = Integer.parseInt(stringsPl[6]);
    }

    public void control(){

        stay();

        if(edge){
            edgeAnim.setStateTime(Claw.stateTime);
            textureRegion = edgeAnim.frame();
        }
        edge=false;
        if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(102))&&!holdLadder){
            super.right = true;
            run();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)&&!holdLadder){
            super.right = false;
            run();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            jumping = true;
            holdLadder = false;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)&&!fall&&jumping){
            jump();
        }
        else{
            jumping = false;
        }


        if(fall){
            fallAnim.setStateTime(Claw.stateTime);
            if(firstFall) {
                if(fallAnim.stateTime==0)
                    firstFall=false;
            }
            while(fallAnim.keyFrame()==0&&!firstFall){
                fallAnim.setStateTime(Claw.stateTime);
            }

            textureRegion = fallAnim.frame();

        }else{
            firstFall=true;
            fallAnim.zeroTime();
        }
        if(!jumping) {
            fall = true;
            jumpY=getY();
        }

        setY(getY()-gravity* Gdx.graphics.getDeltaTime());

        if(SingleGame.ladderArray!=null)
        for (Box x : SingleGame.ladderArray) {
            if (x.checkCollision(getRec())) {
                if(x.numTex==5&&prevY > x.rectangle.y + x.rectangle.height) {
                    if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                        holdLadder=true;
                    }else {
                        setY(prevY);
                        holdLadder=false;
                        fall = false;
                        if((getX()+getWidth()<x.rectangle.getX()+x.rectangle.getWidth()/4&&getX()+getWidth()>x.rectangle.getX())||(getX()>x.rectangle.getX()+x.rectangle.getWidth()*3/4&&getX()<x.rectangle.getX()+x.rectangle.getWidth())){
                            edge=true;
                        }
                    }
                }else {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                        holdLadder = true;
                    }

                }
                if(holdLadder){
                    setX(x.x);
                    fall = false;
                    if(x.numTex==5&&prevY < x.rectangle.y + x.rectangle.height&&getY() > x.rectangle.y + x.rectangle.height-speedLadder * Gdx.graphics.getDeltaTime()) {
                        holdLadder=false;
                        setY(x.rectangle.y + x.rectangle.height+gravity *30* Gdx.graphics.getDeltaTime());
                        //prevY = getY();
                        fall=true;
                    }
                    if(x.numTex==5){
                        ladderUp = true;
                    }else{
                        ladderUp=false;
                    }
                    if(x.numTex==27&&getY()+getHeight()/2 < x.rectangle.y) {
                        holdLadder=false;
                        fall=true;
                    }
                }
            }
        }

        if(fall)holdLadder=false;
        if(holdLadder) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                setY(getY() + speedLadder * Gdx.graphics.getDeltaTime());
                if(!ladderUp) {
                    ladderAnim.setStateTime(Claw.stateTime);
                }else{
                    ladderAnimUp.setStateTime(Claw.stateTime);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                setY(getY() - speedLadder * Gdx.graphics.getDeltaTime());
                if(!ladderUp) {
                    ladderAnim.setStateTime(-Claw.stateTime);
                }else{
                    ladderAnimUp.setStateTime(-Claw.stateTime);
                }
            }
            setY(getY() + gravity * Gdx.graphics.getDeltaTime());
            if(ladderUp)
                textureRegion = ladderAnimUp.frame();
            else {
                textureRegion = ladderAnim.frame();
                ladderAnimUp.zeroTime();
            }
        }



    }

    @Override
    public void run() {
        if(right){
            x+=speed*Gdx.graphics.getDeltaTime();
            runAnim.setStateTime(Claw.stateTime);
            textureRegion = runAnim.frame();
        }
        else{
            x-=speed*Gdx.graphics.getDeltaTime();
            runAnim.setStateTime(Claw.stateTime);
            textureRegion = runAnim.frame();
        }
        if(!jumping&&!fall) {
            if (!changeFoot && !rightRunM.isPlaying() && TimeUtils.millis() - timeStep > difTimeStep) {
                leftRunM.play();
                changeFoot = true;
                timeStep = TimeUtils.millis();
            }
            if (!leftRunM.isPlaying() && changeFoot && TimeUtils.millis() - timeStep > difTimeStep) {
                rightRunM.play();
                changeFoot = false;
                timeStep = TimeUtils.millis();
            }
        }
//        if((x<0||x>game.WIDTH)){
//            if(x<-runAnim.getWidth()&&x>=-200){
//                rightRunM.setVolume(1-x/(-200));
//                leftRunM.setVolume(1-x/(-200));
//            }
//            if(x>game.WIDTH&&x<=game.WIDTH+200){
//                rightRunM.setVolume(1-(x-game.WIDTH)/200);
//                leftRunM.setVolume(1-(x-game.WIDTH)/200);
//            }
//        }
//        else{
//            rightRunM.setVolume(1);
//            leftRunM.setVolume(1);
//        }
    }

    @Override
    public void stay() {
        stayAnim.setStateTime(Claw.stateTime);
        textureRegion = stayAnim.frame();

    }

    @Override
    public void jump() {
        if(getY()-jumpY>=140)    {
            fall=true;
            jumping=false;
        }else {
            jumping=true;
            y += jumpSpeed * (1-(getY()-jumpY)/(getHeight()*2.1)) * Gdx.graphics.getDeltaTime();
            jumpAnim.setStateTime(Claw.stateTime);
            textureRegion = jumpAnim.frame();
        }
    }

    @Override
    public void sitDown() {

    }
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public int getBullet() {
        return bullet;
    }

    public void setBullet(int bullet) {
        this.bullet = bullet;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getTnt() {
        return tnt;
    }

    public void setTnt(int tnt) {
        this.tnt = tnt;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }



    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}

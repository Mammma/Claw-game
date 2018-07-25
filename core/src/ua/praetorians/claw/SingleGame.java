package ua.praetorians.claw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mammma on 19.09.2017.
 */

public class SingleGame implements Screen {
    final Claw game;
    private String level;
    private Player player;
    Rectangle backRec;
    String[] strings;
    Texture background;
    OrthographicCamera camera;
    Vector3 prevCavPos;
    static ArrayList<Box> boxArray,ladderArray,forGround;
    static ArrayList<Rectangle> recTexArray;
    static ArrayList<TextureAnim> spriteAnimBox;
    TextureRegion[] sprites;


    //---------Create---MAP----------
    boolean isCreateMap = false,tempSolid=false,drawPage=false;
    TextureRegion[] temporaryTexture;
    int spritePage=0,numTexture=0;
    float tempX=0,tempY=0;
    Vector3 camVec;

    SingleGame(final Claw game, String level, Player player){
        this.game = game;
        this.level = level;
        camera = new OrthographicCamera(game.WIDTH*3/4, game.HEIGHT*3/4);
        this.player = player;
        strings = getSplitString("player.txt");
        player.setX(Integer.parseInt(strings[0]));
        player.setY(Integer.parseInt(strings[1]));
        System.out.println(player.getX()+" "+player.getY()+" "+player.getHp()+" "+player.getLive());
        camera.position.set((int)(player.getX() + player.getWidth() / 2),(int) (player.getY() + player.getHeight() / 2), 0);
        camera.update();
        player.size = 1f;
        game.manager.load("data/"+level+"/back.jpg",Texture.class);
        game.manager.load("data/"+level+"/sprites.png", Texture.class);
        game.manager.load("data/"+level+"/boxAnim.png", Texture.class);

        while(!game.manager.update()){

        }
        background = game.manager.get("data/"+level+"/back.jpg",Texture.class);
        backRec = new Rectangle(0,0,background.getWidth(),background.getHeight());
        //camera.position.set(player.getX()+player.getWidth()/2,player.getY()+player.getHeight()/2, 0);
        prevCavPos = new Vector3();
        prevCavPos.x = camera.position.x;
        prevCavPos.y = camera.position.y;
        sprites = new TextureAnim().getTextures(game.manager.get("data/"+level+"/sprites.png",Texture.class),11,15);
        recTexArray = getRecTexture(game.manager.get("data/"+level+"/sprites.png",Texture.class));
        boxArray = getDataBox("blocks.txt",sprites);
        ladderArray = getDataBox("ladder.txt",sprites);
        forGround = new ArrayList<Box>();
        int numT=77;
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                if(i<2) {
                    if (j % 2 != 0) numT = 78 + 11 * i;
                    else numT = 77 + 11 * i;
                }else {
                    if (j % 2 != 0) numT = 100;
                    else numT = 99;
                }
                forGround.add(new Box(player.getX()-640+64*j,-130-64*i,64,64,sprites[numT],0,false));
            }
        }


        spriteAnimBox = new ArrayList<TextureAnim>();
        TextureAnim textureAnim = new TextureAnim();
        textureAnim.anima(new TextureRegion(game.manager.get("data/"+level+"/boxAnim.png",Texture.class),0,145,570,28),11,1,0.1f);
        spriteAnimBox.add(textureAnim);
        //--------CREATE__MAP------

    }

    public ArrayList<Box> getDataBox(String fileName, TextureRegion[] t){
        ArrayList<Box> b = new ArrayList<Box>();
        FileHandle boxData = Gdx.files.internal("data/"+level+"/"+fileName);
        String[] stringsBox = boxData.readString().split(";");
        String[] stParam;
        for(String x : stringsBox) {
            stParam = x.split(",");
            b.add(new Box(Float.parseFloat(stParam[0]), Float.parseFloat(stParam[1]), Float.parseFloat(stParam[2]), Float.parseFloat(stParam[3]), t[Integer.parseInt(stParam[4])], Integer.parseInt(stParam[4]), Boolean.parseBoolean(stParam[5])));
        }
        return b;
    }
    public void saveDataBox(){
        FileHandle boxDatas = Gdx.files.local("data/"+level+"/blocks.txt");
        boxDatas.writeString("",false);
        for(Box x : boxArray){
            boxDatas.writeString((int)x.x+","+(int)x.y+","+(int)x.width+","+(int)x.height+","+(int)x.numTex+","+x.solid+";",true);
        }
    }
    public void saveLadderBox(){
        FileHandle boxDatas = Gdx.files.local("data/"+level+"/ladder.txt");
        boxDatas.writeString("",false);
        for(Box x : ladderArray){
            boxDatas.writeString((int)x.x+","+(int)x.y+","+(int)x.width+","+(int)x.height+","+(int)x.numTex+","+x.solid+";",true);
        }
    }

    public ArrayList<Rectangle> getRecTexture(Texture texture){
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        ArrayList<Rectangle> array = new ArrayList<Rectangle>();
        int colum = pixmap.getWidth()/65;
        int row = pixmap.getHeight()/65;
        int i,j,xB,yB,leftX,topY,rightX,bottomY;
        for(int numTex=0;numTex<colum*row;numTex++) {
            i = numTex / colum;
            j = numTex - colum * i;
            xB = 65 * j;
            yB = 65 * i;
            leftX = 64;
            topY = 64;
            rightX = 0;
            bottomY = 0;
            for (int c = 0; c < 64; c++) {
                for (int b = 0; b < 64; b++) {
                    if (pixmap.getPixel(b + xB, c + yB) != -256 && pixmap.getPixel(b + xB, c + yB) != 0) {
                        if (leftX > b) {
                            leftX = b;
                        }
                        if (rightX < b) {
                            rightX = b;
                        }
                        if (topY > c) {
                            topY = c;
                        }
                        if (bottomY < c) {
                            bottomY = c;
                        }
                    }
//                    else{
//                        System.out.println("yeah");
//                        System.out.println(pixmap.getPixel(b+xB,c+yB));
//                    }

                }
            }
            array.add(new Rectangle(leftX, 63 - bottomY, rightX - leftX, bottomY - topY));
        }
        return array;
    }


    public String[] getSplitString(String s){
        FileHandle file = Gdx.files.internal("data/"+level+"/"+s);
        return file.readString().split(";");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(camera.combined);
        camera.update();
        Claw.stateTime = Gdx.graphics.getDeltaTime();
        if(Gdx.input.justTouched()){
            Claw.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(Claw.touchPos);
        }else{
            Claw.touchPos.set(-32000,-32000,0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)){
            if(!isCreateMap)
                isCreateMap = true;
            else
                isCreateMap = false;
        }
        if(!isCreateMap) {
            camera.position.set((int)(player.getX() + player.getWidth() / 2),(int) (player.getY() + player.getHeight() / 2), 0);
            player.control();
            if(prevCavPos.x!=camera.position.x||prevCavPos.y!=camera.position.y){
                backRec.x += (camera.position.x - prevCavPos.x)/4f;
                backRec.y += (camera.position.y - prevCavPos.y)/4f;
                Iterator<Box> iterator = forGround.iterator();
                while (iterator.hasNext()){
                    Box box = iterator.next();
                    box.x -= (camera.position.x - prevCavPos.x)/2;
                    box.y -= (camera.position.y - prevCavPos.y)/2;
                    if(box.x+64<player.getX()-640){
                        box.x+=1280;
                    }
                    if(box.x>player.getX()+640){
                        box.x-=1280;
                    }
                }
                prevCavPos.x = camera.position.x;
                prevCavPos.y = camera.position.y;
            }
        }
        game.batch.begin();
        if(!drawPage) {
            chackBack();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    game.batch.draw(background, backRec.getX() - backRec.getWidth() + backRec.getWidth() * i, backRec.getY() - backRec.getHeight() + backRec.getHeight() * j, backRec.getWidth(), backRec.getHeight());
                }
            }
            for (Box x : boxArray) {
                game.batch.draw(x.texture, x.x, x.y, x.width, x.height);
            }
            for (Box x : ladderArray) {
                game.batch.draw(x.texture, x.x, x.y, x.width, x.height);
            }
            player.draw();
            for (Box x : forGround) {
                game.batch.draw(x.texture, x.x, x.y, x.width, x.height);
            }
        }
        if(isCreateMap)createMap();
        game.batch.end();
    }


    //______----------------------------------------------------------
    //--------------------------CREATE---MAP---------------

    public void createMap(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) spritePage=0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            spritePage = 1;
            temporaryTexture = sprites;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.zoom+=0.01f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.zoom-=0.01f;
        }
//        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) spritePage=2;
//        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) spritePage=3;
        if(spritePage!=0) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
                if(!drawPage){
                    drawPage = true;
                    camVec = new Vector3(camera.position);
                }
                else
                    drawPage=false;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
                if (temporaryTexture.length - 1 > numTexture) {
                    numTexture++;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
                if (numTexture > 0) {
                    numTexture--;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                tempX += temporaryTexture[numTexture].getRegionWidth();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                tempX -= temporaryTexture[numTexture].getRegionWidth();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                tempY += temporaryTexture[numTexture].getRegionHeight();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                tempY -= temporaryTexture[numTexture].getRegionHeight();
            }
            //---------------
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)) {
                tempX += temporaryTexture[numTexture].getRegionWidth()/2;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)) {
                tempX -= temporaryTexture[numTexture].getRegionWidth()/2;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)) {
                tempY += temporaryTexture[numTexture].getRegionHeight()/2;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)) {
                tempY -= temporaryTexture[numTexture].getRegionHeight()/2;
            }
            //-------------
            if (tempX < camera.position.x-camera.viewportWidth/2) {
                camera.position.x = tempX+camera.viewportWidth/2;
            }else
                if( tempX > camera.position.x + camera.viewportWidth/2){
                    camera.position.x = tempX-camera.viewportWidth/2;
                }
            if (tempY < camera.position.y-camera.viewportHeight/2 ) {
                camera.position.y = tempY+camera.viewportHeight/2;
            }else
            if( tempY > camera.position.y + camera.viewportHeight/2){
                camera.position.y = tempY-camera.viewportHeight/2;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
                if(!tempSolid)
                    tempSolid = true;
                else
                    tempSolid = false;
            }

            if(drawPage){
                int j=0,k=0;
                float x=0,y=0;
                for(int i=0;i<temporaryTexture.length;i++) {
                    x = temporaryTexture[i].getRegionWidth()*j+ camVec.x-camera.viewportWidth/2;
                    y = camVec.y+camera.viewportHeight/2-temporaryTexture[i].getRegionHeight()-temporaryTexture[i].getRegionHeight()*k;
                    game.batch.draw(temporaryTexture[i],x,y);
                    if(isTouchObject(x,y,temporaryTexture[i].getRegionWidth(),temporaryTexture[i].getRegionHeight())){
                        numTexture = i;
                    }
                    j++;
                    if(j>10){
                        j=0;
                        k++;
                    }
                }
            }
            game.batch.draw(temporaryTexture[numTexture], tempX, tempY);
            game.font.draw(game.batch,tempSolid?"solid":"notSolid",tempX,tempY);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if(numTexture==5||numTexture==16||numTexture==27){
                    ladderArray.add(new Box(tempX, tempY, temporaryTexture[numTexture].getRegionWidth(), temporaryTexture[numTexture].getRegionHeight(), temporaryTexture[numTexture], numTexture, tempSolid));
                }else {
                    System.out.println(tempX+"  "+tempY);
                    boxArray.add(new Box(tempX, tempY, temporaryTexture[numTexture].getRegionWidth(), temporaryTexture[numTexture].getRegionHeight(), temporaryTexture[numTexture], numTexture, tempSolid));
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.R)){
                Iterator<Box> iterator = boxArray.iterator();
                while (iterator.hasNext()){
                    Box box = iterator.next();
                    if(isTouchObject(box.x,box.y,box.width,box.height)){
                        iterator.remove();
                        System.out.println("remove");
                    }
                }
                Iterator<Box> iterator2 = ladderArray.iterator();
                while (iterator2.hasNext()){
                    Box box = iterator2.next();
                    if(isTouchObject(box.x,box.y,box.width,box.height)){
                        iterator2.remove();
                        System.out.println("remove2");
                    }
                }
            }

            if(Gdx.input.isKeyPressed(Input.Keys.S)&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)&&Gdx.input.isKeyJustPressed(Input.Keys.B)){
                saveDataBox();
                System.out.println("saveDataBox");
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)&&Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)&&Gdx.input.isKeyJustPressed(Input.Keys.L)){
                saveLadderBox();
                System.out.println("saveLadderBox");
            }

        }
    }

    public boolean isTouchObject(float x,float y,float width,float height){
        return Claw.touchPos.x>x&& Claw.touchPos.x<x+width&& Claw.touchPos.y>y&& Claw.touchPos.y<y+height;
    }

    public void chackBack(){
        if(player.getX()<=backRec.getX()){
            backRec.x-=backRec.getWidth();
        }
        if(player.getX()>=backRec.getX()+backRec.getWidth()){
            backRec.x+=backRec.getWidth();
        }
        if(player.getY()<=backRec.getY()){
            backRec.y-=backRec.getHeight();
        }
        if(player.getY()>=backRec.getY()+backRec.getHeight()){
            backRec.y+=backRec.getHeight();
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

    }
}

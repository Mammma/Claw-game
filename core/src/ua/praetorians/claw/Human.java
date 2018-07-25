package ua.praetorians.claw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Mammma on 17.09.2017.
 */

public class Human {
    float x,y,size=1,speed=400,speedLadder=300,prevX,prevY,jumpY,gravity=600,jumpSpeed=1500;
    final Claw game;
    boolean right = true,fall=true,jumping=false,holdLadder=false,edge = false;  // --- which side
    Texture texture;
    TextureRegion textureRegion;
    Rectangle rec,rec2;

    public Human(float x, float y, Texture texture,final Claw game) {
        this.game = game;
        this.x = x;
        this.y = y;
        prevX = x;
        prevY = y;
        this.texture = texture;
        rec = new Rectangle(x,y,64,128);
        rec2 = new Rectangle();
    }

    public void draw(){
        if(SingleGame.boxArray!=null) {
            for (Box x : SingleGame.boxArray) {
                if (x.checkCollision(getRec())) {
                    if(prevY>x.rectangle.getY()+x.rectangle.getHeight()&&(prevX<x.rectangle.getX()+x.rectangle.getWidth()&&prevX+getWidth()>x.rectangle.getX())) {
                        setY(prevY);
                        fall=false;
                        if((getX()+getWidth()<x.rectangle.getX()+x.rectangle.getWidth()/4&&getX()+getWidth()>x.rectangle.getX())||(getX()>x.rectangle.getX()+x.rectangle.getWidth()*3/4&&getX()<x.rectangle.getX()+x.rectangle.getWidth())){
                            edge=true;
                            for(Box y :SingleGame.boxArray){
                                rec2.y = x.rectangle.getY();
                                rec2.width = x.rectangle.getWidth();
                                rec2.height =x.rectangle.getHeight();
                                if(getX()>x.rectangle.getX()+x.rectangle.getWidth()*3/4){
                                    rec2.x=x.rectangle.getX()+x.rectangle.getWidth();
                                }else{
                                    if(getX()+getWidth()<x.rectangle.getX()+x.rectangle.getWidth()/4)
                                        rec2.x=x.rectangle.getX()-x.rectangle.getWidth();
                                }
                                if(y.checkCollision(rec2)){
                                    edge=false;
                                    //break;
                                }
                            }

                        }
                    }else
                    if(x.numTex!=0&&prevY+getHeight()<x.rectangle.getY()&&(prevX<x.rectangle.getX()+x.rectangle.getWidth()&&prevX+getWidth()>x.rectangle.getX())){
                        setY(prevY);
                        fall=true;
                    }else {
                        if(x.numTex!=0&&prevY<x.rectangle.getY()+x.rectangle.getHeight())
                            setX(prevX);
                    }
//
//                    if (!x.checkCollision(getRec(getX(),prevY))){
//                        setY(prevY);
//                    }else
//                    if (!x.checkCollision(getRec(prevX,getY()))){
//                        setX(prevX);
//                    }else {
//                            setX(prevX);
//                            setY(prevY);
//                    }
                }
            }

        }

//        game.batch.draw(textureRegion,right ? x : x+textureRegion.getRegionWidth()*size,y,right ? textureRegion.getRegionWidth()*size : -textureRegion.getRegionWidth()*size,textureRegion.getRegionHeight()*size);
        game.batch.draw(textureRegion,right ? (edge ? getX()-20:getX()) : getX()+(edge ? getWidth() : textureRegion.getRegionWidth()*size),getY()+10,right ? textureRegion.getRegionWidth()*size : -textureRegion.getRegionWidth()*size,textureRegion.getRegionHeight()*size);
        prevX = getX();
        prevY = getY();
    }


    public Rectangle getRec(){
        rec.x = getX();
        rec.y = getY();
        rec.width = getWidth();
        rec.height = getHeight();
        return rec;
    }
    public Rectangle getRec(float x,float y){
        rec.x = x;
        rec.y = y;
        rec.width = getWidth();
        rec.height = getHeight();
        return rec;
    }

//    public float getHeight() {
//        return textureRegion.getRegionHeight()*size;
//    }
//
//    public float getWidth() {
//
//        return textureRegion.getRegionWidth()*size;
//    }
    public float getHeight() {
    return 128;
}

    public float getWidth() {
        return 64;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}

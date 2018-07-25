package ua.praetorians.claw;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Mammma on 05.10.2017.
 */

public class Box {
    float x,y,width,height;
    Rectangle rectangle;
    TextureRegion texture;
    boolean solid;
    int numTex;

    public Box(float x, float y, float width, float height, TextureRegion texture,int numTex, boolean solid) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.solid = solid;
        this.numTex = numTex;
        if(solid){
            rectangle = new Rectangle(x+SingleGame.recTexArray.get(numTex).x, y+SingleGame.recTexArray.get(numTex).y-5, SingleGame.recTexArray.get(numTex).width, SingleGame.recTexArray.get(numTex).height);
            if(numTex==5||numTex==0||numTex==16||numTex==6||numTex==7||numTex==8||numTex==9){
                rectangle.height-=20;
                rectangle.width-=40;
                rectangle.x+=20;
            }
            if(numTex==13){
                rectangle.width+=40;
                rectangle.x-=20;
            }
        }else {
            rectangle = new Rectangle(x, y, width, height);
        }
    }

    public boolean checkCollision(Rectangle rectangle){
        if(this.rectangle.overlaps(rectangle)&&solid){
            return true;
        }else
            return false;
    }

}

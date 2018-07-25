package ua.praetorians.claw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by LaG on 21.10.2015.
 */
public class TextureAnim {
    TextureRegion[] textures,textures2;
    Animation textureAnim;
    float stateTime=0,speed=0;
    int amountSprite;
    float ind;
    boolean fin=false;

    public void anima(TextureRegion textureReg, int column, int row, float speed){
        int index=0;
        this.speed = speed;
        amountSprite = column*row;
        textures = new TextureRegion[amountSprite];
        for(int i=0;i<row;i++)
            for(int j=0;j<column;j++){
                textures[index++] = new TextureRegion(textureReg,j*textureReg.getRegionWidth()/column,i*textureReg.getRegionHeight()/row,textureReg.getRegionWidth()/column,textureReg.getRegionHeight()/row);
            }
        textureAnim = new Animation(speed,textures);

    }

    public TextureRegion[] getTextures(Texture texture, int column, int row){
        int index=0;
        amountSprite = column*row;
        textures = new TextureRegion[amountSprite];
        for(int i=0;i<row;i++)
            for(int j=0;j<column;j++){
                textures[index++] = new TextureRegion(texture,j*texture.getWidth()/column,i*texture.getHeight()/row,texture.getWidth()/column-1,texture.getHeight()/row-1);
            }
       return textures;
    }

    public void anima(Texture texture, int column, int row, int index, float speed, boolean side, int row2, boolean one){
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / column, texture.getHeight() / row);
        this.speed = speed;
        amountSprite = column*row;
        textures = new TextureRegion[amountSprite];
        textures2 = new TextureRegion[column];
        for(int i=0;i<row;i++)
            for(int j=0;j<column;j++){
                if(side) {
                    textures[index++] = tmp[i][j];
                    if(one == true && row2 == i )
                        textures2[j] = tmp[i][j];
                }
                else {
                    textures[index--] = tmp[i][j];

                }
            }
        if(one == true)
            textureAnim = new Animation(speed,textures2);
        else
            textureAnim = new Animation(speed,textures);


    }


    public void setStateTime(float time) {
        fin=false;
        this.stateTime +=time;
        if(this.stateTime>speed*amountSprite){
            fin=true;
           zeroTime();
        }
        if(this.stateTime<0){
            stateTime = speed*amountSprite;
        }

    }


    public void zeroTime(){stateTime=0;}
    public TextureRegion frame(){
        return (TextureRegion) textureAnim.getKeyFrame(stateTime, true);
    }

    public boolean animFin(){
        return textureAnim.isAnimationFinished(stateTime);
    }

    public int keyFrame(){
        return textureAnim.getKeyFrameIndex(stateTime);
    }

    public void setDuration(float speed){
        textureAnim.setFrameDuration(speed);
    }
    public float getSpeed(){return speed;}

    public float getWidth(){
        return textures[0].getRegionWidth();
    }

    public float getHeight() {
        return textures[0].getRegionHeight();
    }

    public TextureRegion getTexture(int i) {
        return textures[i];
    }
}

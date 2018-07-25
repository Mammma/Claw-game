package ua.praetorians.claw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Mammma on 09.09.2017.
 */

public abstract class Button {
    private int x,y,width,height;
    private float size=1;
    long timeTouch = 0;
    private boolean show=true,firstSound=false;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
        width *=size;
        height *=size;
    }

    private Texture quiteTexture, touchTexture;
    private TextureRegion touchTextureReg,quiteTextureReg;


    public TextureRegion getTouchTextureReg() {
        return touchTextureReg;
    }

    public void setTouchTextureReg(TextureRegion touchTextureReg) {
        this.touchTextureReg = touchTextureReg;
    }

    public TextureRegion getQuiteTextureReg() {
        return quiteTextureReg;
    }

    public void setQuiteTextureReg(TextureRegion quiteTextureReg) {
        this.quiteTextureReg = quiteTextureReg;
    }

    public Button(int x, int y, int width, int height, Texture quiteTexture, Texture touchTexture) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        this.quiteTexture = quiteTexture;
        this.touchTexture = touchTexture;
    }
    public Button(int x, int y, int width, int height, TextureRegion quiteTextureReg, TextureRegion touchTextureReg) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.quiteTextureReg = quiteTextureReg;
        this.touchTextureReg = touchTextureReg;
    }
    public Button(TextureRegion quiteTextureReg, TextureRegion touchTextureReg) {
        this.quiteTextureReg = quiteTextureReg;
        this.touchTextureReg = touchTextureReg;
        setWidth(quiteTextureReg.getRegionWidth());
        setHeight(quiteTextureReg.getRegionHeight());
    }
    public Button(TextureRegion quiteTextureReg) {
        this.quiteTextureReg = quiteTextureReg;
        setWidth(quiteTextureReg.getRegionWidth());
        setHeight(quiteTextureReg.getRegionHeight());
    }

    public boolean isTouch() {

        return touch;
    }

    public void setTouch(boolean touch) {
        this.touch = touch;
    }

    public Texture getTouchTexture() {

        return touchTexture;
    }

    public void setTouchTexture(Texture touchTexture) {
        this.touchTexture = touchTexture;
    }

    public Texture getQuiteTexture() {

        return quiteTexture;
    }

    public void setQuiteTexture(Texture quiteTexture) {
        this.quiteTexture = quiteTexture;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {

        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getY() {

        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    private boolean touch=false;

    public void action() {
        if (Claw.touchPos.x > getX() && Claw.touchPos.x < getX() + getWidth() && Claw.touchPos.y > getY() && Claw.touchPos.y < getY() + getHeight()) {
            setTouch(true);
            timeTouch = TimeUtils.millis();
            MainMenu.select.play();
        }
        if (isTouch() && TimeUtils.millis() - timeTouch > 100) {
            setTouch(false);
        }
    }

    public void draw(final Claw game){
        if(isShow()) {
            if (!isTouch() && quiteTextureReg != null) {
                game.batch.draw(quiteTextureReg, getX(), getY(), getWidth(), getHeight());
            } else {
                if (touchTextureReg != null)
                    game.batch.draw(touchTextureReg, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
}

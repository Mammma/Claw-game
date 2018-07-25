package ua.praetorians.claw;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Mammma on 09.09.2017.
 */

public class Scroll {
    private boolean scroll = false;
    private boolean open = false;
    private boolean first = true;

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    private boolean work=false;
    final Claw game;
    private TextureRegion scrollTextureLeft,scrollTextureRight;
    private Texture back,pixmaptex;
    private int x=0,speed=8;
    private Sound sound;

    public Scroll(final Claw game, Texture back) {
        this.game = game;
        scrollTextureLeft = new TextureRegion(game.manager.get("scroll.png",Texture.class),0,0,game.manager.get("scroll.png",Texture.class).getWidth()/2,game.manager.get("scroll.png",Texture.class).getHeight());
        scrollTextureRight = new TextureRegion(game.manager.get("scroll.png",Texture.class),game.manager.get("scroll.png",Texture.class).getWidth()/2,0,game.manager.get("scroll.png",Texture.class).getWidth()/2,game.manager.get("scroll.png",Texture.class).getWidth());
        this.back = back;

        Pixmap pixmap = new Pixmap( 64, 64, Pixmap.Format.RGB888 );
        pixmap.setColor( 0, 0, 0,1 );
        pixmap.fillRectangle(0,0,10,10);
        pixmaptex = new Texture( pixmap );
        pixmap.dispose();
        sound = game.manager.get("Music/rollUp.mp3",Sound.class);
    }

    public void scrolling(){
        if(scroll) {
            if(first) {
                if (!open) {
                    sound.play();
                    x=game.WIDTH/2-scrollTextureLeft.getRegionWidth();
                }
                if (open) {
                    sound.play();
                }
                first=false;
            }
            game.batch.draw(new TextureRegion(back, game.WIDTH-x-scrollTextureLeft.getRegionWidth(), 0, scrollTextureLeft.getRegionWidth(), back.getHeight()), x, 0);
            game.batch.draw(scrollTextureLeft, x, 0,scrollTextureLeft.getRegionWidth(),game.HEIGHT);
            game.batch.draw(pixmaptex, 0, 0,x,game.HEIGHT);
            game.batch.draw(new TextureRegion(back, x, 0, scrollTextureRight.getRegionWidth(), back.getHeight()), game.WIDTH-x-scrollTextureRight.getRegionWidth(), 0);
            game.batch.draw(scrollTextureRight,  game.WIDTH-x-scrollTextureRight.getRegionWidth(), 0,scrollTextureRight.getRegionWidth(),game.HEIGHT);
            game.batch.draw(pixmaptex, game.WIDTH-x, 0,x,game.HEIGHT);
            if (open) x += speed;
            else x -= speed;
            if (x > game.WIDTH/2-scrollTextureLeft.getRegionWidth()) {
                open = false;
                first = true;
                work =false;
            }else
            if (x < -scrollTextureLeft.getRegionWidth()) {
                open = true;
                first = true;
                scroll = false;
                work=false;
                x=-scrollTextureLeft.getRegionWidth();
            }else{
                work = true;
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isScroll() {
        return scroll;
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }
}

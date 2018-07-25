package ua.praetorians.claw;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Mammma on 17.09.2017.
 */

public class Star {
    private Rectangle rectangle;
    private TextureAnim anim;
    private long time;
    int dif;

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    final Claw game;
    private boolean finish=false;
    Star(Rectangle rectangle, final Claw game) {
        this.game = game;
        this.rectangle =  rectangle;
        anim = new TextureAnim();
        anim.anima(game.manager.get("star.png",Texture.class),4,1,0,0.2f,true,1,false);
        time = TimeUtils.millis();
        newDif();
    }

    public void newDif(){
        dif = MathUtils.random(1000,10000);
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void draw(float stateTime){
        if(!finish&&TimeUtils.millis()-time> dif) {
            anim.setStateTime(stateTime);
            game.batch.draw(anim.frame(), rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            if(anim.fin){
                finish=true;
                anim.zeroTime();
                time = TimeUtils.millis();
                newDif();
            }
        }
    }


}

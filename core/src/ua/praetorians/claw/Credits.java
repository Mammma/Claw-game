package ua.praetorians.claw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Mammma on 18.09.2017.
 */

public class Credits implements Screen {
    final Claw game;
    private boolean touch;
    private Rectangle fontRec;
    String[] credits;
    ArrayList<CreditString> strings;
    int i =0;


    public Credits(final Claw game) {
        this.game = game;
        game.scroll.setScroll(true);
        game.scroll.setOpen(false);
        fontRec = new Rectangle(game.WIDTH/6,-40,0,0);
        strings = new ArrayList<CreditString>();
        this.credits = game.credits;
        newString();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        if(Gdx.input.isTouched()){
            Claw.touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(Claw.touchPos);
        }else{
            Claw.touchPos.set(0,0,0);
        }
        game.batch.begin();
        game.batch.draw(game.backLogo,0,0,game.WIDTH,game.HEIGHT);
        game.batch.draw(game.skullLogo,game.WIDTH/2-game.skullLogo.getWidth()/2,0);
        for(CreditString x :strings) {
            x.rectangle.setY(x.rectangle.getY()+100*Gdx.graphics.getDeltaTime());
            game.font.draw(game.batch, x.string, x.rectangle.getX(), x.rectangle.getY());
        }
        Iterator<CreditString> iterator = strings.iterator();
        while (iterator.hasNext()){
            CreditString cred = iterator.next();
            if(cred.rectangle.getY()>game.HEIGHT+40){
                iterator.remove();
            }
        }
        if(strings.size()>=0&&i<credits.length&&strings.get(strings.size()-1).rectangle.getY()>0){
            newString();
        }

        game.scroll.scrolling();
        game.batch.end();


        if((Gdx.input.justTouched() && game.scroll.isOpen())||i>=credits.length){
            touch=true;
            game.scroll.setScroll(true);
        }
        if(!game.scroll.isOpen()&&touch){

            game.setScreen(new MainMenu(game));
        }
    }

    public void newString(){
        if(i>=0) {
            strings.add(new CreditString(credits[i]));
            i++;
        }
    }

    public class CreditString {
        Rectangle rectangle;
        String string;
        CreditString(String s){
            rectangle = new Rectangle(20,-40,0,0);
            string = s;
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



//        try {
//            BufferedReader r = new BufferedReader(new FileReader("CREDITS.txt"));
//            String s;
//            credits="";
//            while((s = r.readLine())!=null){
//                credits+=s+"\n";
//            }
//            r.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
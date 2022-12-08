package com.example.Screens.game.screen.actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Created by Nono on 01/12/2022.
 */
public class Shoot extends Actor {
    private Texture texture;
    private TextureRegion actual;
    private int tiro;
    private boolean tocado = false;


    public Shoot(float posx, float posy, int tiro) {
        texture = new Texture(Gdx.files.internal("sprite_shot.png"));
        actual = new TextureRegion(texture, 21, 28, 29, 16);
        this.tiro = tiro;
        setX(posx);
        setY(posy);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(actual, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.moveBy(tiro * delta, 0);
        if (getX() < -20) {
            tocado = true;
            this.remove();
        }
        if (getX() > 660) {
            tocado = true;
            this.remove();
        }
    }

    public Rectangle getShape() {
        return new Rectangle(getX(), getY(), 25, 25);
    }

    public void setTocado() {
        this.tocado = true;
    }

    public boolean getTocado() {
        return tocado;
    }
}



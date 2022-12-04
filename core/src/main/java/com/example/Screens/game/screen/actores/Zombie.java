package com.example.Screens.game.screen.actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.example.Screens.game.screen.GameScreen;

/**
 * Created by Nono on 27/11/2022.
 */
public class Zombie extends Actor {
    private Animation<TextureRegion> animationDer, animationIzq, animationAtaIzq, animationAtaDer;
    private TextureRegion actual;
    private float stateTime;
    private float posPresaX;
    boolean derecha = false;
    boolean llegado = false;
    private final GameScreen gameScreen;

    public Zombie(float posX, float posY, float posPresaX, GameScreen gameScreen) {
        this.posPresaX = posPresaX;
        this.gameScreen = gameScreen;
        Texture texture = new Texture(Gdx.files.internal("sprite_zombie_der.png"));
        actual = new TextureRegion(texture, 0, 0, 36, 53);

        if (animationDer == null) {
            TextureRegion[] textureRegions = new TextureRegion[]{
                    new TextureRegion(texture, 177, 0, 36, 53),
                    new TextureRegion(texture, 213, 0, 42, 54),
                    new TextureRegion(texture, 256, 0, 37, 51),
                    new TextureRegion(texture, 297, 0, 39, 53),
                    new TextureRegion(texture, 337, 0, 38, 51),
                    new TextureRegion(texture, 380, 0, 36, 49),
            };
            animationDer = new Animation<>(0.0999f, textureRegions);

            textureRegions = new TextureRegion[]{
                    new TextureRegion(texture, 120, 0, 49, 46),
                    new TextureRegion(texture, 77, 0, 44, 49)
            };
            animationAtaDer = new Animation<>(0.1999f, textureRegions);

            texture = new Texture(Gdx.files.internal("sprite_zombie_izq.png"));

            textureRegions = new TextureRegion[]{
                    new TextureRegion(texture, 398, 0, 46, 53),
                    new TextureRegion(texture, 359, 0, 46, 53),
                    new TextureRegion(texture, 318, 0, 46, 53),
                    new TextureRegion(texture, 276, 0, 45, 54),
                    new TextureRegion(texture, 235, 0, 46, 49),
                    new TextureRegion(texture, 194, 0, 43, 51)
            };
            animationIzq = new Animation<>(0.0999f, textureRegions);

            textureRegions = new TextureRegion[]{
                    new TextureRegion(texture, 441, 0, 52, 52),
                    new TextureRegion(texture, 494, 0, 44, 55)
            };
            animationAtaIzq = new Animation<>(0.1999f, textureRegions);
        }
        setX(posX);
        setY(posY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(actual, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        posPresaX = gameScreen.obtenerPosJugador();
        if (posPresaX > getX() + 5 && !llegado) {
            actual = animationDer.getKeyFrame(stateTime, true);
            derecha = true;
            this.moveBy(50 * delta, 0);
        } else if (posPresaX < getX() - 5 && !llegado) {
            actual = animationIzq.getKeyFrame(stateTime, true);
            derecha = false;
            this.moveBy(-50 * delta, 0);
        } else {
            if (derecha) {
                actual = animationAtaDer.getKeyFrame(stateTime, true);
            } else {
                actual = animationAtaIzq.getKeyFrame(stateTime, true);
            }
        }


    }
}

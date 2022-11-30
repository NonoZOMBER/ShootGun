package com.example.Screens.game.screen.actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Nono on 27/11/2022.
 */
public class Soldier extends Actor {
    enum HorizontalMovement {LEFT, NONE, RIGHT}
    private float posx;
    private final float posy;
    private TextureRegion actual;
    private Animation<TextureRegion> caminarA, caminarD, morir;

    HorizontalMovement horizontalMovement;
    private final TextureRegion reposoD, reposoI;
    private float timeFarmer;
    boolean derecha = false;
    Texture tSoldier;

    public Soldier(float posx, float posy) {
        this.posx = posx;
        this.posy = posy;
        tSoldier = new Texture(Gdx.files.internal("soldier_sprite.png"));
        if (caminarA == null) {
            TextureRegion[] caminarAlante = new TextureRegion[]{
                    new TextureRegion(tSoldier, 0, 254, 45, 50),
                    new TextureRegion(tSoldier, 45, 254, 45, 50),
                    new TextureRegion(tSoldier, 90, 254, 45, 50),
                    new TextureRegion(tSoldier, 135, 254, 45, 50),
                    new TextureRegion(tSoldier, 180, 254, 45, 50),
                    new TextureRegion(tSoldier, 225, 254, 45, 50),
            };
            caminarA = new Animation<>(0.099f, caminarAlante);
        }

        reposoD = new TextureRegion(tSoldier, 270, 254, 45, 50);
        tSoldier = new Texture(Gdx.files.internal("soldier_sprite_B.png"));
        if (caminarD == null) {
            TextureRegion[] caminarAlante = new TextureRegion[]{
                    new TextureRegion(tSoldier, 315, 254, 45, 50),
                    new TextureRegion(tSoldier, 270, 254, 45, 50),
                    new TextureRegion(tSoldier, 225, 254, 45, 50),
                    new TextureRegion(tSoldier, 180, 254, 45, 50),
                    new TextureRegion(tSoldier, 135, 254, 45, 50),
                    new TextureRegion(tSoldier, 90, 254, 45, 50),
            };
            caminarD = new Animation<>(0.099f, caminarAlante);
        }

        reposoI = new TextureRegion(tSoldier, 45, 254, 45, 50);
        actual = reposoD;

        timeFarmer = 0f;

        addListener(new FarmerInputListener());

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

        timeFarmer += delta;
        if (horizontalMovement == HorizontalMovement.LEFT) {
            this.moveBy(-200 * delta, 0);
        }
        if (horizontalMovement == HorizontalMovement.RIGHT) {
            this.moveBy(200 * delta, 0);
        }

        if (horizontalMovement == HorizontalMovement.NONE) {
            if (derecha) {
                actual = reposoD;
            } else {
                actual = reposoI;
            }
        }

        if (horizontalMovement == HorizontalMovement.RIGHT) {
            actual = caminarA.getKeyFrame(timeFarmer, true);
            derecha = true;
        }

        if (horizontalMovement == HorizontalMovement.LEFT) {
            actual = caminarD.getKeyFrame(timeFarmer, true);
            derecha = false;
        }

        if (getX() < 0) setX(0);
        if (getX() > 640 - getWidth()) setX(640 - getWidth());

    }

    class FarmerInputListener extends InputListener {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.LEFT:
                    horizontalMovement = HorizontalMovement.LEFT;
                    break;
                case Input.Keys.RIGHT:
                    horizontalMovement = HorizontalMovement.RIGHT;
                    break;
            }
            return true;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.LEFT:
                    if (horizontalMovement == HorizontalMovement.LEFT) {
                        horizontalMovement = HorizontalMovement.NONE;
                    }
                    break;
                case Input.Keys.RIGHT:
                    if (horizontalMovement == HorizontalMovement.RIGHT) {
                        horizontalMovement = HorizontalMovement.NONE;
                    }
                    break;
            }
            return true;
        }
    }
}

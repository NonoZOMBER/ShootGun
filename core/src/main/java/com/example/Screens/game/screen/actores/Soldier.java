package com.example.Screens.game.screen.actores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.example.Screens.game.screen.GameScreen;

/**
 * Created by Nono on 27/11/2022.
 */
public class Soldier extends Actor {
    enum HorizontalMovement {LEFT, NONE, RIGHT}

    private TextureRegion actual;
    private Animation<TextureRegion> caminarA, caminarD, morir;
    HorizontalMovement horizontalMovement;
    private final TextureRegion reposoD, reposoI;
    private float timeFarmer;
    boolean derecha = false;
    Texture tSoldier;
    private boolean dispara;
    private long timeDisparo;
    private Sound sound;
    private GameScreen gameScreen;
    private boolean inmortal = false;
    private boolean muerto = false;
    private float timeInmortal;
    private float timeVisible;

    private int countInmortal;


    private boolean tocado = false;

    public Soldier(float posx, float posy, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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

        sound = Gdx.audio.newSound(Gdx.files.internal("sound_shoot.mp3"));

        reposoI = new TextureRegion(tSoldier, 45, 254, 45, 50);
        actual = reposoD;

        timeFarmer = 0f;

        addListener(new FarmerInputListener());

        timeVisible = 0f;
        timeInmortal = 0f;
        countInmortal = 5;

        setX(posx);
        setY(posy);

        dispara = true;

        this.setSize(45, 50);

        timeInmortal = TimeUtils.nanoTime();
        timeVisible = TimeUtils.nanoTime();

        actualizarDisparo();
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

        if (inmortal) {
            activarInmortalidad(delta);
        } else {
            setVisible(true);
        }

        if (horizontalMovement == HorizontalMovement.RIGHT) {
            actual = caminarA.getKeyFrame(timeFarmer, true);
            derecha = true;
        }

        if (horizontalMovement == HorizontalMovement.LEFT) {
            actual = caminarD.getKeyFrame(timeFarmer, true);
            derecha = false;
        }

        if (TimeUtils.nanoTime() - timeDisparo > 222255555) {
            dispara = true;
            actualizarDisparo();
        }


        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && dispara) {
            Vector3 vector = new Vector3();
            vector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            getStage().getCamera().unproject(vector);
            int tiro;
            if (Gdx.input.getX() > getX()) {
                tiro = 500;
                actual = reposoD;
                derecha = true;
                Shoot shoot = new Shoot(getX() + getWidth(), getY() + (getHeight() / 2) + 1, tiro);
                getStage().addActor(shoot);
                gameScreen.saveShoot(shoot);
            }
            if (Gdx.input.getX() < getX()) {
                tiro = -500;
                actual = reposoI;
                derecha = false;
                Shoot shoot = new Shoot(getX() - 25, getY() + (getHeight() / 2) + 1, tiro);
                getStage().addActor(shoot);
                gameScreen.saveShoot(shoot);
            }
            sound.play();
            dispara = false;
        }
        gameScreen.updatePosPlayer(getX());
        if (getX() < 0) setX(0);
        if (getX() > 640) setX(640);

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

    /*====================================================== METODOS ======================================================*/

    private void actualizarDisparo() {
        timeDisparo = TimeUtils.nanoTime();
    }

    public boolean getInmortal() {
        return inmortal;
    }

    public void setInmortal() {
        inmortal = !inmortal;
    }

    private void activarInmortalidad(float delta) {
        timeVisible += delta;
        timeInmortal += delta;
        if (timeInmortal >= 1 && countInmortal != 0) {
            countInmortal--;
            timeInmortal = 0;
        } else if (countInmortal == 0) {
            inmortal = false;
            countInmortal = 5;
        }

        if (timeVisible >= 0.2f) {
            setVisible(!isVisible());
            timeVisible = 0;
        }

    }

    public boolean getMuerto() {
        return muerto;
    }

    public Rectangle getShape() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void setMuerto() {
        this.muerto = true;
    }
}

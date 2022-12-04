package com.example.Screens.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Screens.game.screen.actores.Soldier;
import com.example.Screens.game.screen.actores.Zombie;
import com.example.Screens.options.screen.nextlevel.screen.LoadLevel;
import com.example.load.skin.ShootGun;

/**
 * Created by Nono on 24/11/2022.
 */
public class GameScreen extends ScreenAdapter {
    private ShootGun game;
    private Stage stage;
    private Texture tShoot;
    private Texture heart;
    private TextureRegion heartVivo;
    private TextureRegion heartGastado;
    private int score;
    private long timeSecond;
    private long timeZombie;
    private long timeLevel;
    private int porcentaje;
    private int contSecond;
    private Array<Image> corazones;
    private Table hud;
    private OrthographicCamera camera;
    private int vidas;
    private Soldier soldier;
    private int nivel;
    Table corazonesLayout;
    Label scoreLabel;
    Label timeLabel;
    float posJugadorX;
    float posJugadorY;

    public GameScreen(ShootGun game, int nivel, int score) {
        this.game = game;
        cargarDificultad();
        tShoot = new Texture(Gdx.files.internal("sprite_shot.png"));

        if (heartVivo == null) {
            heart = new Texture(Gdx.files.internal("heart_sprite.png"));
            heartVivo = new TextureRegion(heart, 7, 38, 281, 241);
            heartGastado = new TextureRegion(heart, 593, 47, 281, 241);
        }
        this.nivel = nivel;
        corazones = new Array<>(3);
        vidas = 3;
        this.score = score;
        camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport(camera));
        timeSecond = 0;
        contSecond = 100;
        cargarHud();
        stage.addActor(hud);
        actualizarSeconds();
        timeZombie = TimeUtils.nanoTime();
        posJugadorX = camera.viewportWidth / 2;
        cargarDificultad();
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (TimeUtils.nanoTime() - timeSecond > 1000000000) {
            contSecond--;
            actualizarTime();
            actualizarSeconds();
            if (contSecond == 0) game.setScreen(new LoadLevel(game, nivel + 1, score));
        }
        if (TimeUtils.nanoTime() - timeZombie > timeLevel) creadorZombies();


        camera.update();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.clear();
        actualizarHud();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        heart.dispose();
        tShoot.dispose();
    }

    /*====================================================== METODOS ======================================================*/
    private void actualizarSeconds() {
        timeSecond = TimeUtils.nanoTime();
    }

    private void cargarHud() {
        hud = new Table();
        scoreLabel = new Label("Score: " + this.score, game.gameSkin);
        hud.add(scoreLabel);
        corazonesLayout = new Table();
        for (int i = 0; i < vidas; i++) {
            Image image = new Image(heartVivo);
            this.corazones.add(image);
            corazonesLayout.add(image).spaceLeft(10).height(50).size(25, 25);
        }
        hud.add(corazonesLayout).width(200).spaceLeft(camera.viewportWidth / 5);
        timeLabel = new Label("Time: " + contSecond, game.gameSkin);
        hud.add(timeLabel).spaceLeft(camera.viewportWidth / 5);
        actualizar();
    }

    private void actualizarHud() {
        hud = new Table();
        scoreLabel = new Label("Score: " + this.score, game.gameSkin);
        hud.add(scoreLabel);
        corazonesLayout = new Table();
        for (int i = 0; i < this.corazones.size; i++) {
            corazonesLayout.add(this.corazones.get(i)).spaceLeft(10).height(50).size(25, 25);
        }
        hud.add(corazonesLayout).width(200).spaceLeft(camera.viewportWidth / 5);
        timeLabel = new Label("Time: " + contSecond, game.gameSkin);
        hud.add(timeLabel).spaceLeft(camera.viewportWidth / 5);
        actualizar();
    }

    private void actualizar() {
        stage.setViewport(new ScreenViewport(camera));
        hud.setPosition(camera.viewportWidth / 2, camera.viewportHeight - 30);
        if (stage.getActors().isEmpty()) {
            soldier = new Soldier(camera.viewportWidth / 2, camera.viewportHeight / 3, this);
            stage.addActor(soldier);
            stage.setKeyboardFocus(soldier);
        }
        stage.addActor(new Zombie(-10, camera.viewportHeight / 3, posJugadorX, this));
        stage.addActor(hud);
        show();
    }

    private void actualizarTime() {
        timeLabel.setText("Time: " + contSecond);
    }

    private void actualizarScore() {
        scoreLabel.setText("Score: " + score);
    }

    public void actualizarPosJugador(float x) {
        posJugadorX = x;
    }

    public float obtenerPosJugador() {
        return posJugadorX;
    }

    private void creadorZombies() {
        int posivilidad = MathUtils.random(0, 100);
        if (posivilidad < porcentaje) {
            switch (MathUtils.random(1)) {
                case 0:
                    stage.addActor(new Zombie(-20, camera.viewportHeight / 3, posJugadorX, this));
                    break;
                case 1:
                    stage.addActor(new Zombie(camera.viewportWidth + 20, camera.viewportHeight / 3, posJugadorX, this));
                    break;
            }
        }
        timeZombie = TimeUtils.nanoTime();
    }

    private void cargarDificultad() {
        switch (nivel) {
            case 0:
                timeLevel = 1000000000;
                porcentaje = 20;
                break;
            case 1:
                timeZombie = 100000000;
                porcentaje = 40;
                break;
            case 2:
                timeZombie = 10000000;
                porcentaje = 50;
                break;
            default:
                timeZombie = 1000000;
                porcentaje = 70;
                break;
        }
    }

}

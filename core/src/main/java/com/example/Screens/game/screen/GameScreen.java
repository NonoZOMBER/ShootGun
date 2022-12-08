package com.example.Screens.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Screens.game.screen.actores.Shoot;
import com.example.Screens.game.screen.actores.Soldier;
import com.example.Screens.game.screen.actores.Zombie;
import com.example.Screens.options.screen.gameover.screen.GameOver;
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
    private int percentage;
    private int contSecond;
    private Array<Image> hearts;
    private Table hud;
    private OrthographicCamera camera;
    private int vidas;
    private Soldier soldier;
    private final int level;
    Table heartsLayout;
    Label scoreLabel;
    Label timeLabel;
    float posPlayerX;
    Array<Zombie> zombies = new Array<>();
    Array<Shoot> shoots = new Array<>();
    Music fonNight;
    private TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    int[] base = {0};
    int[] decoracion = {1};

    public GameScreen(ShootGun game, int level, int score) {
        this.game = game;
        loadDifficulty();
        map = new TmxMapLoader().load("map_shoot-gun.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        tShoot = new Texture(Gdx.files.internal("sprite_shot.png"));
        fonNight = Gdx.audio.newMusic(Gdx.files.internal("sonido_fondo_city.mp3"));
        fonNight.setLooping(true);
        fonNight.play();
        if (heartVivo == null) {
            heart = new Texture(Gdx.files.internal("heart_sprite.png"));
            heartVivo = new TextureRegion(heart, 7, 38, 281, 241);
            heartGastado = new TextureRegion(heart, 593, 47, 281, 241);
        }
        this.level = level;
        hearts = new Array<>(3);
        vidas = 3;
        this.score = score;
        camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport(camera));
        timeSecond = 0;
        contSecond = 100;
        loadHud();
        stage.addActor(hud);
        updateSeconds();
        timeZombie = TimeUtils.nanoTime();
        posPlayerX = camera.viewportWidth / 2;
        loadDifficulty();
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
            updateTime();
            updateSeconds();
            if (contSecond == 0) {
                silenciarZombies();
                fonNight.stop();
                game.setScreen(new LoadLevel(game, level + 1, score));
            }
        }

        if (TimeUtils.nanoTime() - timeZombie > timeLevel) generatorZombies();

        for (int i = 0; i < shoots.size; i++) {
            for (int j = 0; j < zombies.size; j++) {
                if (!shoots.get(i).getTocado() && !zombies.get(j).getMuerto() && Intersector.overlaps(shoots.get(i).getShape(), zombies.get(j).getShape())) {
                    zombies.get(j).matar();
                    shoots.get(i).setTocado();
                    shoots.get(i).remove();
                    score++;
                    updateScore();
                }
            }
        }

        for (int i = 0; i < zombies.size; i++) {
            if (!zombies.get(i).getMuerto() && zombies.get(i).atacando && !soldier.getInmortal() && Intersector.overlaps(zombies.get(i).getShape(), soldier.getShape())) {
                if (vidas > 0 && zombies.get(i).atacando) {
                    vidas--;
                    soldier.setInmortal();
                    actualizarCorazones();
                }
            }
        }

        if (vidas == 0) {
            soldier.setMuerto();
            fonNight.stop();
            silenciarZombies();
            game.setScreen(new GameOver(game, score, level));
        }

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.act();
        stage.draw();
        mapRenderer.render(decoracion);
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
    private void updateSeconds() {
        timeSecond = TimeUtils.nanoTime();
    }

    private void loadHud() {
        hud = new Table();
        scoreLabel = new Label("Score: " + this.score, game.gameSkin);
        hud.add(scoreLabel);
        heartsLayout = new Table();
        for (int i = 0; i < 3; i++) {
            Image image = new Image(heartVivo);
            hearts.add(image);
            heartsLayout.add(image).spaceLeft(10).height(50).size(25, 25);
        }
        hud.add(heartsLayout).width(200).spaceLeft(camera.viewportWidth / 5);
        timeLabel = new Label("Time: " + contSecond, game.gameSkin);
        hud.add(timeLabel).spaceLeft(camera.viewportWidth / 5);
        update();
    }

    private void updateHud() {
        hud = new Table();
        scoreLabel = new Label("Score: " + this.score, game.gameSkin);
        hud.add(scoreLabel);
        heartsLayout = new Table();
        for (int i = 0; i < this.hearts.size; i++) {
            heartsLayout.add(this.hearts.get(i)).spaceLeft(10).height(50).size(25, 25);
        }
        hud.add(heartsLayout).width(200).spaceLeft(camera.viewportWidth / 5);
        timeLabel = new Label("Time: " + contSecond, game.gameSkin);
        hud.add(timeLabel).spaceLeft(camera.viewportWidth / 5);
        update();
    }

    private void actualizarCorazones() {
        heartsLayout.clear();
        heartsLayout.add(new Image(heartVivo)).spaceLeft(10).height(50).size(25, 25);
        if (vidas == 2) {
            heartsLayout.add(new Image(heartVivo)).spaceLeft(10).height(50).size(25, 25);
            heartsLayout.add(new Image(heartGastado)).spaceLeft(10).height(50).size(25, 25);
        } else if (vidas == 1) {
            heartsLayout.add(new Image(heartGastado)).spaceLeft(10).height(50).size(25, 25);
            heartsLayout.add(new Image(heartGastado)).spaceLeft(10).height(50).size(25, 25);
        }
    }

    private void update() {
        stage.setViewport(new ScreenViewport(camera));
        hud.setPosition(camera.viewportWidth / 2, camera.viewportHeight - 30);
        if (stage.getActors().isEmpty()) {
            soldier = new Soldier(camera.viewportWidth / 2, 50, this);
            stage.addActor(soldier);
            stage.setKeyboardFocus(soldier);
        }

        stage.addActor(hud);
        show();
    }

    private void updateTime() {
        timeLabel.setText("Time: " + contSecond);
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    public void updatePosPlayer(float x) {
        posPlayerX = x;
    }

    public float getPosPlayer() {
        return posPlayerX;
    }

    private void generatorZombies() {
        int posivilidad = MathUtils.random(0, 100);
        if (posivilidad < percentage) {
            switch (MathUtils.random(1)) {
                case 0:
                    Zombie zombie = new Zombie(-20, 50, posPlayerX, this);
                    stage.addActor(zombie);
                    zombies.add(zombie);
                    break;
                case 1:
                    Zombie zombie1 = new Zombie(camera.viewportWidth + 20, 50, posPlayerX, this);
                    stage.addActor(zombie1);
                    zombies.add(zombie1);
                    break;
            }
        }
        timeZombie = TimeUtils.nanoTime();
    }

    private void loadDifficulty() {
        switch (level) {
            case 0:
                timeLevel = 1000000000;
                percentage = 40;
                break;
            case 1:
                timeZombie = 100000000;
                percentage = 50;
                break;
            case 2:
                timeZombie = 10000000;
                percentage = 70;
                break;
            default:
                timeZombie = 1000000;
                percentage = 95;
                break;
        }
    }

    public void saveShoot(Shoot shoot) {
        shoots.add(shoot);
    }

    private void silenciarZombies() {
        for (Zombie zombie : zombies) {
            zombie.silenciar();
            zombie.matar();
        }
    }

}

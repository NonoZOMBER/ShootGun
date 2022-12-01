package com.example.Screens.options.screen.menu.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Screens.game.screen.GameScreen;
import com.example.Screens.options.screen.list.score.screen.ListScoreScreen;
import com.example.Screens.options.screen.nextlevel.screen.LoadLevel;
import com.example.load.skin.ShootGun;
import com.example.tools.GifDecoder;

/**
 * Created by Nono on 24/11/2022.
 */
public class OptionsMenu extends ScreenAdapter {

    private final ShootGun game;
    private Stage stage;
    private OrthographicCamera camera;
    private TextButton scoreButton, playButton, exitButton;
    private Label titulo;
    private SpriteBatch batch;
    private Animation<TextureRegion> animacionFondo;
    private float time;
    private Music musicaFondo;

    public OptionsMenu(ShootGun game) {
        this.game = game;
        batch = new SpriteBatch();
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("MusicFondo.ogg"));
        animacionFondo = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("fondo.gif").read());
        camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport(camera));
        time = 0f;
        actualizarMenu();
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        time += delta;
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.begin();
        batch.draw(animacionFondo.getKeyFrame(time), 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();
        stage = new Stage(new ScreenViewport(camera));
        batch = new SpriteBatch();
        actualizarMenu();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    /*====================================================== METODOS ======================================================*/

    private void actualizarMenu() {

        Table table = new Table();

        titulo = new Label("Shoot-Gun", game.gameSkin, "title");
        titulo.setColor(Color.WHITE);
        titulo.setAlignment(Align.center);
        table.add(titulo).space(90f);
        table.row();

        playButton = new TextButton("Play", game.gameSkin, "menu-item");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                musicaFondo.dispose();
                game.setScreen(new LoadLevel(game, 0));
            }
        });

        table.add(playButton).width(300f).height(playButton.getHeight()).space(30f).bottom();
        table.row();

        scoreButton = new TextButton("Scores", game.gameSkin, "menu-item");
        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                musicaFondo.stop();
                game.setScreen(new ListScoreScreen(game));
            }
        });

        table.add(scoreButton).width(300f).space(30f).bottom();
        table.row();


        exitButton = new TextButton("Exit", game.gameSkin, "menu-item");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });

        table.add(exitButton).width(300f).space(30f);

        table.setPosition(
                camera.viewportWidth / 2,
                camera.viewportHeight / 2 - table.getHeight());

        stage.addActor(table);
        show();
    }


}

package com.example.Screens.options.screen.gameover.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Screens.options.screen.list.score.screen.GuardarPuntuacion;
import com.example.Screens.options.screen.menu.screen.OptionsMenu;
import com.example.load.skin.ShootGun;
import com.example.tools.GifDecoder;

/**
 * Created by Nono on 07/12/2022.
 */
public class GameOver extends ScreenAdapter {

    private ShootGun game;
    private int score;
    private int level;
    private Stage stage;
    private OrthographicCamera camera;
    private Label scoreLabel, levelLabel;
    private TextField txtName;
    private TextButton btnGuardar, btnVolverMenu;
    private SpriteBatch batch;
    private Animation<TextureRegion> animacionFondo;
    private float time;
    private Music musicaFondo;

    public GameOver(ShootGun game, int score, int level) {
        this.game = game;
        this.score = score;
        this.level = level;
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
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.begin();
        batch.draw(animacionFondo.getKeyFrame(time), 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        musicaFondo.dispose();
    }

    /*====================================================== METODOS ======================================================*/
    private void actualizarMenu() {
        Table table = new Table();
        levelLabel = new Label("Level: " + level, game.gameSkin, "title");
        levelLabel.setAlignment(Align.center);
        table.add(levelLabel).space(30f);
        table.row();

        scoreLabel = new Label("Score: " + score, game.gameSkin, "title");
        scoreLabel.setAlignment(Align.center);
        table.add(scoreLabel).space(30f);
        table.row();

        txtName = new TextField("", game.gameSkin, "login");
        txtName.setMessageText("NICK");
        table.add(txtName).width(200f).space(30f);
        table.row();

        btnGuardar = new TextButton("SAVE SCORE", game.gameSkin, "menu-item");
        btnGuardar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (!txtName.getText().isEmpty()) {
                    GuardarPuntuacion gp = new GuardarPuntuacion();
                    gp.saveScore(txtName.getText(), score);
                    musicaFondo.stop();
                    game.setScreen(new OptionsMenu(game));
                }
            }
        });
        table.add(btnGuardar).width(300f).space(30f).bottom();
        table.row();

        btnVolverMenu = new TextButton("BACK TO MENU", game.gameSkin, "menu-item");
        btnVolverMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                musicaFondo.stop();
                game.setScreen(new OptionsMenu(game));
            }
        });
        table.add(btnVolverMenu).width(300f).space(30f).bottom();

        table.setPosition(
                camera.viewportWidth / 2,
                camera.viewportHeight / 2 - table.getHeight());

        stage.addActor(table);
        show();
    }
}

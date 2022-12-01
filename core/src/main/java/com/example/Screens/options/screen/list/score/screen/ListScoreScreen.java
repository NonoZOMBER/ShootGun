package com.example.Screens.options.screen.list.score.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Screens.options.screen.menu.screen.OptionsMenu;
import com.example.load.skin.ShootGun;

/**
 * Created by Nono on 24/11/2022.
 */
public class ListScoreScreen extends ScreenAdapter {
    private ShootGun game;
    private List<String> listaElementos;
    private OrthographicCamera camera;
    private TextButton volverButton;
    private Label titulo;
    private Stage stage;
    private Music musicaFondo;

    public ListScoreScreen(ShootGun game) {
        this.game = game;
        listaElementos = new List<>(game.gameSkin);
        listaElementos.setItems(new CargarPuntuaciones().getScore());
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("MusicFondo.ogg"));
        camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport(camera));
        musicaFondo.setLooping(true);
        musicaFondo.play();
        actualizarVentana();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();
        stage = new Stage(new ScreenViewport(camera));
        actualizarVentana();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    /*====================================================== METODOS ======================================================*/

    private void actualizarVentana() {
        Table table = new Table();

        titulo = new Label("Scores", game.gameSkin, "title");
        titulo.setColor(Color.WHITE);
        titulo.setAlignment(Align.center);
        table.add(titulo).space(90f);
        table.row();

        listaElementos.setAlignment(Align.center);
        table.add(listaElementos).height(200f).width(400f);
        table.row();


        volverButton = new TextButton("Volver", game.gameSkin, "menu-item");
        volverButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                musicaFondo.stop();
                game.setScreen(new OptionsMenu(game));
            }
        });

        table.add(volverButton).width(300f).height(volverButton.getHeight()).space(30f).bottom();

        table.setPosition(
                camera.viewportWidth / 2,
                camera.viewportHeight / 2 - table.getHeight());

        stage.addActor(table);
        show();
    }
}

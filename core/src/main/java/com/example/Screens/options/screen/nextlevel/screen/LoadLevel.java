package com.example.Screens.options.screen.nextlevel.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Screens.game.screen.GameScreen;
import com.example.Screens.options.screen.menu.screen.OptionsMenu;
import com.example.Screens.score.screen.AddScoreScreen;
import com.example.load.skin.ShootGun;

/**
 * Created by Nono on 30/11/2022.
 */
public class LoadLevel extends ScreenAdapter {
    private final ShootGun game;
    private final int nivel;
    private final int score;
    private final Stage stage;
    private final OrthographicCamera camera;

    public LoadLevel(ShootGun game, int nivel, int score) {
        this.game = game;
        this.nivel = nivel;
        this.score = score;
        camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport(camera));
        stage.addActor(new Image(new Texture(Gdx.files.internal("fondoLevel.png"))));
        cargarScreen();
    }

    public LoadLevel(ShootGun game, int nivel) {
        this(game, nivel, 0);
    }

    private void cargarScreen() {
        Table table = new Table();
        Label nivelLabel;
        if (this.nivel == 0) {
            nivelLabel = new Label("Level 0", game.gameSkin, "title");
        } else {
            nivelLabel = new Label("Lavel " + (this.nivel - 1) + " - " + this.nivel, game.gameSkin, "title");
        }
        table.add(nivelLabel).space(50f);
        table.row();

        Label scoreLabel = new Label("Score: " + this.score, game.gameSkin, "title");
        table.add(scoreLabel).space(10f);
        table.row();

        TextButton continuar = new TextButton("PLAY", game.gameSkin, "menu-item");
        continuar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game, nivel, score));
            }
        });
        table.add(continuar).width(300f).space(30f).bottom();
        table.row();

        TextButton volverMenu = new TextButton("BACK TO MENU", game.gameSkin, "menu-item");
        volverMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (nivel == 0) {
                    game.setScreen(new OptionsMenu(game));
                } else if (nivel > 0) {
                    game.setScreen(new AddScoreScreen(game, score));
                }
            }
        });
        table.add(volverMenu).width(300f).space(10f).bottom();
        table.row();

        table.setPosition(
                camera.viewportWidth / 2,
                camera.viewportHeight / 2 - table.getHeight() + 60
        );

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {Gdx.input.setInputProcessor(null);}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

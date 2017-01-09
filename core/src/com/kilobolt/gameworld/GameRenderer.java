package com.kilobolt.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.kilobolt.TweenAccessors.Value;
import com.kilobolt.TweenAccessors.ValueAccessor;
import com.kilobolt.gameobjects.Bird;
import com.kilobolt.gameobjects.Grass;
import com.kilobolt.gameobjects.Pipe;
import com.kilobolt.gameobjects.ScrollHandler;
import com.kilobolt.gameworld.GameWorld;
import com.kilobolt.ui.SimpleButton;
import com.kilobolt.zbHelpers.AssetLoader;
import com.kilobolt.zbHelpers.InputHandler;

import java.util.List;

import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

public class GameRenderer {
    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;
    private int midPointY;
    private Bird bird;
    private ScrollHandler scroller;
    private Grass frontGrass;
    private Grass backGrass;
    private Pipe pipe1;
    private Pipe pipe2;
    private Pipe pipe3;
    private TextureRegion bg;
    private TextureRegion grass;
    private Animation birdAnimation;
    private TextureRegion birdMid;
    private TextureRegion birdDown;
    private TextureRegion birdUp;
    private TextureRegion skullUp;
    private TextureRegion skullDown;
    private TextureRegion bar;

    private TweenManager manager;
    private Value alpha = new Value();
    private List<SimpleButton> menuButtons;

    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
        this.myWorld = world;
        this.midPointY = midPointY;
        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();
        this.cam = new OrthographicCamera();
        this.cam.setToOrtho(true, 136.0F, (float)gameHeight);
        this.batcher = new SpriteBatch();
        this.batcher.setProjectionMatrix(this.cam.combined);
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setProjectionMatrix(this.cam.combined);
        this.initGameObjects();
        this.initAssets();
        setupTweens();
    }
    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
                .start(manager);
    }

    public void render(float delta,float runTime) {
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.shapeRenderer.begin(ShapeType.Filled);
        this.shapeRenderer.setColor(0.21568628F, 0.3137255F, 0.39215687F, 1.0F);
        this.shapeRenderer.rect(0.0F, 0.0F, 136.0F, (float)(this.midPointY + 66));
        this.shapeRenderer.setColor(0.43529412F, 0.7294118F, 0.1764706F, 1.0F);
        this.shapeRenderer.rect(0.0F, (float)(this.midPointY + 66), 136.0F, 11.0F);
        this.shapeRenderer.setColor(0.5764706F, 0.3137255F, 0.105882354F, 1.0F);
        this.shapeRenderer.rect(0.0F, (float)(this.midPointY + 77), 136.0F, 52.0F);
        this.shapeRenderer.end();
        this.batcher.begin();
        this.batcher.disableBlending();
        this.batcher.draw(this.bg, 0.0F, (float)(this.midPointY + 23), 136.0F, 43.0F);
        this.drawGrass();
        this.drawPipes();
        this.batcher.enableBlending();
        this.drawSkulls();
        if (myWorld.isRunning()) {
            drawBird(runTime);
            drawScore();
        } else if (myWorld.isReady()) {
            drawBird(runTime);
            drawScore();
        } else if (myWorld.isMenu()) {
            drawBirdCentered(runTime);
            drawMenuUI();
        } else if (myWorld.isGameOver()) {
            drawBird(runTime);
            drawScore();
        } else if (myWorld.isHighScore()) {
            drawBird(runTime);
            drawScore();
        }

        batcher.end();
        drawTransition(delta);
    }
    private void drawTransition(float delta) {
        if (alpha.getValue() > 0) {
            manager.update(delta);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, alpha.getValue());
            shapeRenderer.rect(0, 0, 136, 300);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

        }
    }

    private void initGameObjects() {
        this.bird = this.myWorld.getBird();
        this.scroller = this.myWorld.getScroller();
        this.frontGrass = this.scroller.getFrontGrass();
        this.backGrass = this.scroller.getBackGrass();
        this.pipe1 = this.scroller.getPipe1();
        this.pipe2 = this.scroller.getPipe2();
        this.pipe3 = this.scroller.getPipe3();
    }

    private void initAssets() {
        this.bg = AssetLoader.bg;
        this.grass = AssetLoader.grass;
        this.birdAnimation = AssetLoader.birdAnimation;
        this.birdMid = AssetLoader.bird;
        this.birdDown = AssetLoader.birdDown;
        this.birdUp = AssetLoader.birdUp;
        this.skullUp = AssetLoader.skullUp;
        this.skullDown = AssetLoader.skullDown;
        this.bar = AssetLoader.bar;
    }

    private void drawGrass() {
        this.batcher.draw(this.grass, this.frontGrass.getX(), this.frontGrass.getY(), (float)this.frontGrass.getWidth(), (float)this.frontGrass.getHeight());
        this.batcher.draw(this.grass, this.backGrass.getX(), this.backGrass.getY(), (float)this.backGrass.getWidth(), (float)this.backGrass.getHeight());
    }

    private void drawSkulls() {
        this.batcher.draw(this.skullUp, this.pipe1.getX() - 1.0F, this.pipe1.getY() + (float)this.pipe1.getHeight() - 14.0F, 24.0F, 14.0F);
        this.batcher.draw(this.skullDown, this.pipe1.getX() - 1.0F, this.pipe1.getY() + (float)this.pipe1.getHeight() + 45.0F, 24.0F, 14.0F);
        this.batcher.draw(this.skullUp, this.pipe2.getX() - 1.0F, this.pipe2.getY() + (float)this.pipe2.getHeight() - 14.0F, 24.0F, 14.0F);
        this.batcher.draw(this.skullDown, this.pipe2.getX() - 1.0F, this.pipe2.getY() + (float)this.pipe2.getHeight() + 45.0F, 24.0F, 14.0F);
        this.batcher.draw(this.skullUp, this.pipe3.getX() - 1.0F, this.pipe3.getY() + (float)this.pipe3.getHeight() - 14.0F, 24.0F, 14.0F);
        this.batcher.draw(this.skullDown, this.pipe3.getX() - 1.0F, this.pipe3.getY() + (float)this.pipe3.getHeight() + 45.0F, 24.0F, 14.0F);
    }

    private void drawPipes() {
        this.batcher.draw(this.bar, this.pipe1.getX(), this.pipe1.getY(), (float)this.pipe1.getWidth(), (float)this.pipe1.getHeight());
        this.batcher.draw(this.bar, this.pipe1.getX(), this.pipe1.getY() + (float)this.pipe1.getHeight() + 45.0F, (float)this.pipe1.getWidth(), (float)(this.midPointY + 66 - (this.pipe1.getHeight() + 45)));
        this.batcher.draw(this.bar, this.pipe2.getX(), this.pipe2.getY(), (float)this.pipe2.getWidth(), (float)this.pipe2.getHeight());
        this.batcher.draw(this.bar, this.pipe2.getX(), this.pipe2.getY() + (float)this.pipe2.getHeight() + 45.0F, (float)this.pipe2.getWidth(), (float)(this.midPointY + 66 - (this.pipe2.getHeight() + 45)));
        this.batcher.draw(this.bar, this.pipe3.getX(), this.pipe3.getY(), (float)this.pipe3.getWidth(), (float)this.pipe3.getHeight());
        this.batcher.draw(this.bar, this.pipe3.getX(), this.pipe3.getY() + (float)this.pipe3.getHeight() + 45.0F, (float)this.pipe3.getWidth(), (float)(this.midPointY + 66 - (this.pipe3.getHeight() + 45)));
    }
    private void drawBirdCentered(float runTime) {
        batcher.draw(birdAnimation.getKeyFrame(runTime), 59, bird.getY() - 15,
                bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
                bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
    }
    private void drawBird(float runTime) {

        if (bird.shouldntFlap()) {
            batcher.draw(birdMid, bird.getX(), bird.getY(),
                    bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
                    bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());

        } else {
            batcher.draw(birdAnimation.getKeyFrame(runTime), bird.getX(),
                    bird.getY(), bird.getWidth() / 2.0f,
                    bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(),
                    1, 1, bird.getRotation());
        }

    }
    private void drawMenuUI() {
        batcher.draw(AssetLoader.zbLogo, 136 / 2 - 56, midPointY - 50,
                AssetLoader.zbLogo.getRegionWidth() / 1.2f,
                AssetLoader.zbLogo.getRegionHeight() / 1.2f);

        for (SimpleButton button : menuButtons) {
            button.draw(batcher);
        }

    }
    private void drawScore() {
        int length = ("" + myWorld.getScore()).length();
        AssetLoader.shadow.draw(batcher, "" + myWorld.getScore(),
                68 - (3 * length), midPointY - 82);
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(),
                68 - (3 * length), midPointY - 83);
    }
}

package com.kilobolt.gameobjects;

import com.kilobolt.gameworld.GameWorld;
import com.kilobolt.zbHelpers.AssetLoader;

public class ScrollHandler {
    private Grass frontGrass;
    private Grass backGrass;
    private Pipe pipe1;
    private Pipe pipe2;
    private Pipe pipe3;
    private GameWorld gameWorld;
    public static final int SCROLL_SPEED = -59;
    public static final int PIPE_GAP = 49;

    public ScrollHandler(GameWorld gameWorld, float yPos) {
        this.gameWorld = gameWorld;
        this.frontGrass = new Grass(0.0F, yPos, 143, 11, SCROLL_SPEED);
        this.backGrass = new Grass(this.frontGrass.getTailX(), yPos, 143, 11, SCROLL_SPEED);
        this.pipe1 = new Pipe(210.0F, 0.0F, 22, 60, -59.0F, yPos);
        this.pipe2 = new Pipe(this.pipe1.getTailX() + 49.0F, 0.0F, 22, 70, SCROLL_SPEED, yPos);
        this.pipe3 = new Pipe(this.pipe2.getTailX() + 49.0F, 0.0F, 22, 60, SCROLL_SPEED, yPos);
    }
    public void updateReady(float delta) {

        frontGrass.update(delta);
        backGrass.update(delta);

        if (frontGrass.isScrolledLeft()) {
            frontGrass.reset(backGrass.getTailX());

        } else if (backGrass.isScrolledLeft()) {
            backGrass.reset(frontGrass.getTailX());

        }

    }

    public void update(float delta) {
        this.frontGrass.update(delta);
        this.backGrass.update(delta);
        this.pipe1.update(delta);
        this.pipe2.update(delta);
        this.pipe3.update(delta);
        if(this.pipe1.isScrolledLeft()) {
            this.pipe1.reset(this.pipe3.getTailX() + PIPE_GAP);
        } else if(this.pipe2.isScrolledLeft()) {
            this.pipe2.reset(this.pipe1.getTailX() + PIPE_GAP);
        } else if(this.pipe3.isScrolledLeft()) {
            this.pipe3.reset(this.pipe2.getTailX() + PIPE_GAP);
        }

        if(this.frontGrass.isScrolledLeft()) {
            this.frontGrass.reset(this.backGrass.getTailX());
        } else if(this.backGrass.isScrolledLeft()) {
            this.backGrass.reset(this.frontGrass.getTailX());
        }

    }

    public Grass getFrontGrass() {
        return this.frontGrass;
    }

    public Grass getBackGrass() {
        return this.backGrass;
    }

    public Pipe getPipe1() {
        return this.pipe1;
    }

    public Pipe getPipe2() {
        return this.pipe2;
    }

    public Pipe getPipe3() {
        return this.pipe3;
    }

    public void stop() {
        this.frontGrass.stop();
        this.backGrass.stop();
        this.pipe1.stop();
        this.pipe2.stop();
        this.pipe3.stop();
    }

    public boolean collides(Bird bird) {
        if(!this.pipe1.isScored() && this.pipe1.getX() + (float)(this.pipe1.getWidth() / 2) < bird.getX() + bird.getWidth()) {
            this.addScore(1);
            this.pipe1.setScored(true);
            AssetLoader.coin.play();
        } else if(!this.pipe2.isScored() && this.pipe2.getX() + (float)(this.pipe2.getWidth() / 2) < bird.getX() + bird.getWidth()) {
            this.addScore(1);
            this.pipe2.setScored(true);
            AssetLoader.coin.play();
        } else if(!this.pipe3.isScored() && this.pipe3.getX() + (float)(this.pipe3.getWidth() / 2) < bird.getX() + bird.getWidth()) {
            this.addScore(1);
            this.pipe3.setScored(true);
            AssetLoader.coin.play();
        }

        return this.pipe1.collides(bird) || this.pipe2.collides(bird) || this.pipe3.collides(bird);
    }

    private void addScore(int increment) {
        this.gameWorld.addScore(increment);
    }
    public void onRestart(){
        frontGrass.onRestart(0, SCROLL_SPEED);
        backGrass.onRestart(frontGrass.getTailX(),SCROLL_SPEED);
        pipe1.onRestart(210, SCROLL_SPEED);
        pipe2.onRestart(pipe1.getTailX() + PIPE_GAP,SCROLL_SPEED);
        pipe3.onRestart(pipe2.getTailX() + PIPE_GAP,SCROLL_SPEED);
    }
}

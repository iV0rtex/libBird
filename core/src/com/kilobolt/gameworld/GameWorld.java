package com.kilobolt.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.kilobolt.gameobjects.Bird;
import com.kilobolt.gameobjects.ScrollHandler;
import com.kilobolt.zbHelpers.AssetLoader;

public class GameWorld {
    private Bird bird;
    private ScrollHandler scroller;
    private Rectangle ground;
    private int score = 0;
    private GameState currentState;
    public int midPointY;
    private float runTime = 0;

    public GameWorld(int midPointY) {
        currentState = GameState.MENU;
        this.midPointY = midPointY;
        this.bird = new Bird(33.0F, (float)(midPointY - 5), 17, 12);
        this.scroller = new ScrollHandler(this, (float)(midPointY + 66));
        this.ground = new Rectangle(0.0F, (float)(midPointY + 66), 136.0F, 11.0F);
    }
    public enum GameState{
        MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
    }

    public void updateRunning(float delta) {

        if(delta > 0.15F) {
            delta = 0.15F;
        }
        this.bird.update(delta);
        this.scroller.update(delta);
        if(this.scroller.collides(this.bird) && this.bird.isAlive()) {
            this.scroller.stop();
            this.bird.die();
            AssetLoader.dead.play();
        }

        if(Intersector.overlaps(this.bird.getBoundingCircle(), this.ground)) {
            this.scroller.stop();
            this.bird.die();
            this.bird.decelerate();
            currentState = GameState.GAMEOVER;
            if(score>AssetLoader.getHighScore()){
                AssetLoader.setHighScore(score);
                currentState = GameState.HIGHSCORE;

            }
        }

    }
    public void restart(){
        currentState = GameState.READY;
        score = 0;
        bird.onRestart(midPointY - 5);
        scroller.onRestart();
        currentState = GameState.READY;
    }
    public void update(float delta){
        runTime += delta;
        switch (currentState){
            case READY:
                updateReady(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            default:
                break;
        }
    }
    private void updateReady(float delta){
        bird.updateReady(runTime);
        scroller.updateReady(delta);
    }

    public Bird getBird() {
        return this.bird;
    }

    public int getMidPointY(){
        return midPointY;
    }

    public ScrollHandler getScroller() {
        return this.scroller;
    }

    public int getScore() {
        return this.score;
    }

    public void addScore(int increment) {
        this.score += increment;
    }
    public boolean isReady(){
        return currentState == GameState.READY;
    }
    public void start(){
        currentState = GameState.RUNNING;
    }
    public void ready() {
        currentState = GameState.READY;
    }
    public boolean isGameOver(){
        return currentState == GameState.GAMEOVER;
    }
    public boolean isHighScore(){
        return currentState == GameState.HIGHSCORE;
    }
    public boolean isMenu() {
        return currentState == GameState.MENU;
    }
    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }
}

package com.kilobolt.gameobjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class Pipe extends Scrollable {
    private Random r = new Random();
    private Rectangle skullUp = new Rectangle();
    private Rectangle skullDown = new Rectangle();
    private Rectangle barUp = new Rectangle();
    private Rectangle barDown = new Rectangle();
    public static final int VERTICAL_GAP = 45;
    public static final int SKULL_WIDTH = 24;
    public static final int SKULL_HEIGHT = 11;
    private float groundY;
    private boolean isScored = false;

    public Pipe(float x, float y, int width, int height, float scrollSpeed, float groundY) {
        super(x, y, width, height, scrollSpeed);
        this.groundY = groundY;
    }

    public void reset(float newX) {
        super.reset(newX);
        this.height = this.r.nextInt(90) + 15;
        this.isScored = false;
    }

    public Rectangle getSkullUp() {
        return this.skullUp;
    }

    public Rectangle getSkullDown() {
        return this.skullDown;
    }

    public Rectangle getBarUp() {
        return this.barUp;
    }

    public Rectangle getBarDown() {
        return this.barDown;
    }

    public void update(float delta) {
        super.update(delta);
        this.barUp.set(this.position.x, this.position.y, (float)this.width, (float)this.height);
        this.barDown.set(this.position.x, this.position.y + this.height + VERTICAL_GAP,this.width, this.groundY - (this.position.y + this.height + VERTICAL_GAP));
        this.skullUp.set(this.position.x - ((SKULL_WIDTH - this.width) / 2), this.position.y + this.height - SKULL_HEIGHT, SKULL_WIDTH, SKULL_HEIGHT);
        this.skullDown.set(this.position.x - ((SKULL_WIDTH - this.width) / 2), this.barDown.y, SKULL_WIDTH, SKULL_HEIGHT);
    }

    public boolean collides(Bird bird) {
        return this.position.x >= bird.getX() + bird.getWidth()?false:Intersector.overlaps(bird.getBoundingCircle(), this.barUp) || Intersector.overlaps(bird.getBoundingCircle(), this.barDown) || Intersector.overlaps(bird.getBoundingCircle(), this.skullUp) || Intersector.overlaps(bird.getBoundingCircle(), this.skullDown);
    }

    public boolean isScored() {
        return this.isScored;
    }

    public void setScored(boolean b) {
        this.isScored = b;
    }
    public void onRestart(float x,float scrollSpeed){
        velocity.x = scrollSpeed;
        reset(x);
    }
}

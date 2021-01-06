package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    Texture bucket;
    Texture drop;

    Rectangle bucketBox;

    private static final float BUCKET_SIZE = 48;
    private static final float BUCKET_SPEED = 1000;

    private static final float DROP_SPEED = 300;
    private static final float DROP_SIZE = 48;

    private static final float dropDeltaTime = 100;

    private long dropDeltaCurrentTime;

    ArrayList<Rectangle> drops = new ArrayList<>();

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
//        batch.setProjectionMatrix(camera.combined);

        bucket = new Texture("bucket.png");
        drop = new Texture(Gdx.files.internal("drop.png"));

        bucketBox = new Rectangle(Gdx.graphics.getWidth() - BUCKET_SIZE / 2, 20, 48, 48);
        createDrop();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.begin();
        // Draw here
        batch.draw(bucket, bucketBox.x, bucketBox.y, bucketBox.width, bucketBox.height);
        for (Rectangle dropBox : drops) {
            batch.draw(drop, dropBox.x, dropBox.y, dropBox.width, dropBox.height);
        }
        batch.end();

        for (Iterator<Rectangle> iterator = drops.iterator(); iterator.hasNext(); ) {
            Rectangle dropBox = iterator.next();
            dropBox.y -= DROP_SPEED * Gdx.graphics.getDeltaTime();
            if (dropBox.y < 0 || dropBox.overlaps(bucketBox)) {
                iterator.remove();
            }
        }

        if (bucketBox.x > Gdx.graphics.getWidth() - BUCKET_SIZE) {
            bucketBox.x = Gdx.graphics.getWidth() - BUCKET_SIZE;
        }
        if (bucketBox.x < 0) {
            bucketBox.x = 0;
        }

        if (Gdx.input.isTouched()) {
            bucketBox.x = Gdx.input.getX() - BUCKET_SIZE / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketBox.x += BUCKET_SPEED * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketBox.x -= BUCKET_SPEED * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.rotate(10);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(-10);
        }

        if (TimeUtils.timeSinceMillis(dropDeltaCurrentTime) > dropDeltaTime) {
            createDrop();
        }
    }

    public void createDrop() {
        int x = new Random().nextInt((int) (Gdx.graphics.getWidth() - DROP_SIZE));
        Rectangle dropBox = new Rectangle(x, Gdx.graphics.getHeight() - DROP_SIZE, DROP_SIZE, DROP_SIZE);
        drops.add(dropBox);
        dropDeltaCurrentTime = System.currentTimeMillis();
    }

    @Override
    public void dispose() {
        bucket.dispose();
        drop.dispose();
        batch.dispose();
    }
}

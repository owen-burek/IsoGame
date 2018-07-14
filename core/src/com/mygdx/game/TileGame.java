package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TileGame extends ApplicationAdapter {
	private TiledMap map;
	private IsometricTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Texture character;
	private TextureRegion[] characterPoses;
	private SpriteBatch batch;
	private Matrix4 isoMatrix;
	private Viewport viewport;

	@Override
	public void create() {

		isoMatrix = new Matrix4().idt()
				.scale((float) (Math.sqrt(2.0) / 2.0), (float) (Math.sqrt(2.0) / 4.0), 1.0f)
				.scale(2,2,1) // Fixes placement problem with tilemaps
				.rotate(0.0f, 0.0f, 1.0f, -45);


		batch = new SpriteBatch();
		// Load map from Tiled and create isometric renderer
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(20, 20, camera);
		viewport.apply();

		map = new TmxMapLoader().load("Test_Map.tmx");
		renderer = new IsometricTiledMapRenderer(map, 1 / 32f, batch);

		Vector3 vec = new Vector3(5,5, 0).mul(isoMatrix);
		camera.translate(vec.x, vec.y);
		camera.update();
		// Load character spritesheet, split poses into separate textures
		character = new Texture(Gdx.files.internal("Sprites/Characters/new-sprites.png"));
		TextureRegion[][] splitPoses = TextureRegion.split(character, character.getWidth() / 4, character.getHeight());
		characterPoses = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			characterPoses[i] = splitPoses[0][i];
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Vector3 vec = new Vector3(1, 1, 0);
		vec.mul(isoMatrix);
		renderer.setView(camera);
		renderer.render();
		viewport.apply();
		batch.begin();
		batch.draw(characterPoses[1], vec.x, vec.y + 0.5f, 2, 1);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}
}

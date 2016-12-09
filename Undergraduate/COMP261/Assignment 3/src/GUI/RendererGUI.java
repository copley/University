package GUI;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import Lighting.DefaultLightSource;
import Lighting.LightSource;
import Reader.Reader;
import Reader.SceneReader;
import Renderer.Renderer;
import Shader.DefaultShader;
import Shader.Shader;

public class RendererGUI extends GUI {

	private Renderer renderer;
	
	@Override
	protected void onLoad(File file) {
		Reader<Scene> reader = new SceneReader(file);
		Scene s = reader.read();
		
		lightsListModel.clear();
		lightsListModel.addElement(s.getLightSources().get(0));
		
		renderer = new Renderer(reader, GUI.CANVAS_WIDTH, GUI.CANVAS_HEIGHT);
	}

	@Override
	protected void onKeyPress(KeyEvent ev) {
		if (renderer == null) {
			return;
		}
		
		if (ev.getKeyCode() == KeyEvent.VK_EQUALS) {
			renderer.updateAmbientLight(0.1f);
		} else if (ev.getKeyCode() == KeyEvent.VK_MINUS) {
			renderer.updateAmbientLight(-0.1f);
		} else if (ev.getKeyCode() == KeyEvent.VK_LEFT) {
			renderer.rotate(0, 0.1);
		} else if (ev.getKeyCode() == KeyEvent.VK_RIGHT) {
			renderer.rotate(0, -0.1);
		} else if (ev.getKeyCode() == KeyEvent.VK_UP) {
			renderer.rotate(0.1, 0);
		} else if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
			renderer.rotate(-0.1, 0);
		}
		
		
	}

	@Override
	protected BufferedImage render() {
		if (renderer == null) {
			return null;
		}
		
		renderer.setAmbientLightComponents(this.getAmbientLight());
		
		return renderer.render();
	}
	
	public static void main(String[] args) {
		new RendererGUI();
	}

	@Override
	protected void onLightSourceAdd(LightSource l) {
		if (renderer == null) {
			return;
		}
		
		renderer.getScene().getLightSources().add(l);
	}

	@Override
	protected void onLightSourceRemove(LightSource l) {
		if (renderer == null) {
			return;
		}
		
		renderer.getScene().getLightSources().remove(l);
	}

	@Override
	protected void onShaderChange(Shader s) {
		renderer.setShader(s);
		
	}

}

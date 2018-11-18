package Tools;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private static boolean A, S, D, W, SPACE, ESC, ENTER, F, CTRL;
	private static boolean ARROW_RIGHT, ARROW_LEFT, ARROW_UP;
	private static boolean keyIsPressed;

	public KeyInput() {
		A = false;
		S = false;
		D = false;
		W = false;
		SPACE = false;
		ESC = false;
		ENTER = false;
		F = false;
		CTRL = false;
		keyIsPressed = false;
		ARROW_RIGHT = false;
		ARROW_LEFT = false;
		ARROW_UP = false;
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_A)
			A = true;
		if (keyCode == KeyEvent.VK_D)
			D = true;
		if (keyCode == KeyEvent.VK_S)
			S = true;
		if (keyCode == KeyEvent.VK_W)
			W = true;
		if (keyCode == KeyEvent.VK_SPACE)
			SPACE = true;
		if (keyCode == KeyEvent.VK_ENTER)
			ENTER = true;
		if (keyCode == KeyEvent.VK_F)
			F = true;
		if (keyCode == KeyEvent.VK_CONTROL)
			CTRL = true;
		if (keyCode == KeyEvent.VK_ESCAPE)
			ESC = true;
		if (keyCode == KeyEvent.VK_L) 
			ARROW_RIGHT = true;
		if (keyCode == KeyEvent.VK_J) 
			ARROW_LEFT = true;
		if (keyCode == KeyEvent.VK_I) 
			ARROW_UP = true;
		
		keyIsPressed = true;
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_A)
			A = false;
		if (keyCode == KeyEvent.VK_D)
			D = false;
		if (keyCode == KeyEvent.VK_S)
			S = false;
		if (keyCode == KeyEvent.VK_W)
			W = false;
		if (keyCode == KeyEvent.VK_SPACE)
			SPACE = false;
		if (keyCode == KeyEvent.VK_ENTER)
			ENTER = false;
		if (keyCode == KeyEvent.VK_F)
			F = false;
		if (keyCode == KeyEvent.VK_CONTROL)
			CTRL = false;
		if (keyCode == KeyEvent.VK_ESCAPE)
			ESC = false;
		if (keyCode == KeyEvent.VK_L) 
			ARROW_RIGHT = false;
		if (keyCode == KeyEvent.VK_J) 
			ARROW_LEFT = false;
		if (keyCode == KeyEvent.VK_I) 
			ARROW_UP = false;
		keyIsPressed = false;
	}

	public static boolean isA() {
		return A;
	}

	public static boolean isS() {
		return S;
	}

	public static boolean isD() {
		return D;
	}

	public static boolean isW() {
		return W;
	}

	public static boolean isSPACE() {
		return SPACE;
	}

	public static boolean isESC() {
		return ESC;
	}

	public static boolean isENTER() {
		return ENTER;
	}

	public static boolean isF() {
		return F;
	}

	public static boolean isCTRL() {
		return CTRL;
	}

	public static boolean isARROW_RIGHT() {
		return ARROW_RIGHT;
	}

	public static boolean isARROW_LEFT() {
		return ARROW_LEFT;
	}

	public static boolean isARROW_UP() {
		return ARROW_UP;
	}

	public static void setS(boolean s) {
		S = s;
	}

	public static void setW(boolean w) {
		W = w;
	}

	public static void setA(boolean a) {
		A = a;
	}

	public static void setD(boolean d) {
		D = d;
	}

	public static boolean isKeyIsPressed() {
		return keyIsPressed;
	}
	
	
}

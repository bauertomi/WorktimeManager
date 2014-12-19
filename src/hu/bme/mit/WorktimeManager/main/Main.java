package hu.bme.mit.WorktimeManager.main;

import hu.bme.mit.WorktimeManager.gui.AppWindow;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author BlackBeard A program belépési pontja
 *
 */
public class Main {

	public final static int WINDOW_SIZE_X = 800;
	public final static int WINDOW_SIZE_Y = 600;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}
				showWindow();
			}
		});
	}

	private static void showWindow() {
		AppWindow appWindow = new AppWindow();
		appWindow.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);

		appWindow.setVisible(true);

	}

}

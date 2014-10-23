package hu.bme.mit.WorktimeManager.gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class AppWindow extends JFrame {
	private static final long serialVersionUID = 8996973239562737076L;
	/**
	 * A felhasznĂˇlĂłi felĂĽlet paneljainak azonosĂ­tĂˇsĂˇra szolgĂˇlĂł enum
	 */
	public enum PanelId {
		IP_PANEL, APP_FIELD
	}

	private Map<PanelId, AppPanel> mPanels = new HashMap<>();
	private AppPanel mCurrentPanel = null;

	public AppWindow() {
		super("Worktime Manager");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		//initComponents();
		//mAppManager = new AppManager((AppField) mPanels.get(PanelId.APP_FIELD));
	}

/*	private void initComponents() {
		mPanels.put(PanelId.SERVER_PANEL, new ServerPanel(this));
		mPanels.put(PanelId.CLIENT_PANEL, new ClientPanel(this));

	}*/

	/**
	 * A {@link PanelId}-val azonosĂ­tott felhasznĂˇlĂłi felĂĽlet elem megjelenĂ­tĂ©se
	 * 
	 * @param panelId
	 */
	public void showPanel(final PanelId panelId) {
		if (mCurrentPanel != null) {
			mCurrentPanel.onHide();
		}

		AppPanel newPanel = mPanels.get(panelId);
		setContentPane(newPanel);
		validate();
		newPanel.requestFocusInWindow();
		newPanel.onShow();
		mCurrentPanel = newPanel;
	}
}

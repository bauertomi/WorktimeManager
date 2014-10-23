package hu.bme.mit.WorktimeManager.gui;

import hu.bme.mit.WorktimeManager.gui.AppWindow.PanelId;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public abstract class AppPanel extends JPanel {
	private static final long serialVersionUID = -7570121177800374977L;

	protected final AppWindow mAppWindow;
	protected static final Font mHeaderFont = new Font("Serif", Font.BOLD, 30);
	protected static final Font mStandardFont = new Font("Serif", Font.PLAIN, 20);
	protected static final Font mButtonFont = new Font("Serif", Font.PLAIN, 20);

	protected static Image mBackgroundImage;
	protected JButton mBtnBack;
	protected JLabel mHeaderLabel;

	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	public AppPanel(AppWindow appWindow) {
		mAppWindow = appWindow;
		try {
			mBackgroundImage = ImageIO
					.read(AppPanel.class.getResourceAsStream("/hu/bme/mit/asteroid/res/space_1.jpg"));
			UIManager.put("Label.foreground", Color.WHITE);
		} catch (IOException e) {
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(mBackgroundImage, 0, 0, null);
	}

	/**
	 * A panelokon megjelenĹ‘ Vissza gomb pĂ©ldĂˇnyosĂ­tĂˇsa, mĹ±kĂ¶dĂ©sĂ©nek definiĂˇlĂˇsa
	 * 
	 * @param panelId
	 *            Melyik panelre lĂ©pjĂĽnk vissza?
	 * @return A Vissza gomb
	 */
	protected JButton getBackButton(final PanelId panelId) {
		if (mBtnBack == null) {

			mBtnBack = new JButton("Vissza");
			mBtnBack.setFont(mButtonFont);
			mBtnBack.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					mAppWindow.showPanel(panelId);
				}
			});
		}
		return mBtnBack;
	}

	protected JLabel getHeaderLabel(String title) {
		if (mHeaderLabel == null) {
			mHeaderLabel = new JLabel(title);
			mHeaderLabel.setFont(mHeaderFont);
			mHeaderLabel.setForeground(Color.WHITE);
			mHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
		}
		return mHeaderLabel;
	}

	/**
	 * Akkor hĂ­vĂłdik, amikor a panel megjelenik
	 */
	protected void onShow() {

	}

	/**
	 * Akkor hĂ­vĂłdik, amikor a panel helyĂ©re mĂˇsik kerĂĽl
	 */
	protected void onHide() {

	}
}

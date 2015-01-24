package hu.bme.mit.WorktimeManager.main;

import hu.bme.mit.WorktimeManager.gui.AppWindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Storage {
	private static final String STORAGE_PATH = "storage.txt";
	private ArrayList<Record> mRecords = new ArrayList<Record>();
	private ArrayList<StorageListener> mListeners = new ArrayList<StorageListener>();
	private static Storage instance = null;

	public interface StorageListener {
		public void onRowAdded(Record record);
	}

	protected Storage() {

	}

	public static Storage getInstance() {
		if (instance == null) {
			instance = new Storage();
		}
		return instance;
	}

	public void registerStorageListener(StorageListener listener) {
		mListeners.add(listener);
	}

	public void unregisterStorageListener(StorageListener listener) {
		mListeners.remove(listener);
	}

	public Record getRow(int id) {
		return mRecords.get(id);
	}

	public void addRow(Record record) {
		mRecords.add(record);

		for (StorageListener storageListener : mListeners) {
			storageListener.onRowAdded(record);
		}

		/**
		 * Itt hivodik meg a mentes. Itt kell? Itt is es a storage-ban is
		 * mukodik.
		 */
		/*
		 * try { instance.saveStorage(record); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}

	public static void saveStorage(Record record) throws IOException {

		File file = new File(STORAGE_PATH);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String line = sdf.format(record.getTimeStamp()) + "-" + record.getID();

		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line);
		bw.newLine();
		bw.close();

		System.out.println("Done");

	}

	public static void readStorage() throws IOException {

		BufferedReader br = null;
		String sCurrentLine;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String text = new String();

		br = new BufferedReader(new FileReader(STORAGE_PATH));

		while ((sCurrentLine = br.readLine()) != null) {

			String[] l = sCurrentLine.split("-");

			try {
				sdf.format((Date) sdf.parse(l[0]));
				date = sdf.parse(l[0]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			text = sCurrentLine.substring(sCurrentLine.lastIndexOf('-') + 1);

			Record record = new Record(text, date);
			instance.addRow(record);
		}
		br.close();
	}

}
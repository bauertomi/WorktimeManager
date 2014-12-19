package hu.bme.mit.WorktimeManager.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {
	public static final String STORAGE_PATH = "C:\\Users\\BlackBeard\\Desktop\\storage.txt";
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
	}

	public static void saveStorage(String args) throws IOException {
		String content = args;

		File file = new File(STORAGE_PATH);

		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		System.out.println("Done");

	}

	public static String readStorage(String path) throws IOException {

		BufferedReader br = null;
		String sCurrentLine;

		br = new BufferedReader(new FileReader(path));

		while ((sCurrentLine = br.readLine()) != null) {
			System.out.println(sCurrentLine);
		}
		br.close();
		return sCurrentLine;

	}

}

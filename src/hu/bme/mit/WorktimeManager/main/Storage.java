package hu.bme.mit.WorktimeManager.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {
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

		File file = new File("C:\\Users\\BlackBeard\\Desktop\\storage.txt");

		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		System.out.println("Done");

		}

	}

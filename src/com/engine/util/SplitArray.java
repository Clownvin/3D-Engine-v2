package com.engine.util;

import java.util.ArrayList;

public final class SplitArray <T> {
	private final ArrayList<ArrayList<T>> arrayLists = new ArrayList<ArrayList<T>>();
	
	public SplitArray(T[] array, int lists) {
		arrayLists.ensureCapacity(lists);
		for (int i = 0, idx  = 0; i < lists; i++) {
			int listLength = (array.length / lists) + (i == lists - 1 ? array.length % lists : 0);
			arrayLists.add(new ArrayList<T>(listLength));
			for (int j = 0; j < listLength; j++) {
				arrayLists.get(i).add(array[idx++]);
			}
		}
	}
	
	public synchronized ArrayList<T> getList(int index) {
		return arrayLists.get(index);
	}
	
	public synchronized int getListCount() {
		return arrayLists.size();
	}
}

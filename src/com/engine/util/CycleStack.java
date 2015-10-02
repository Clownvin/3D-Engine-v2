package com.engine.util;

import java.util.ConcurrentModificationException;

//Rewritten
public class CycleStack<T> {

	private transient volatile Object[] arrayData;

	private int takePointer = 0;
	private int addPointer = 0;
	private int size = 0;
	private volatile long modCount = 0L;

	private static final int DEFAULT_CAPACITY = 10;
	private static final Object[] EMPTY_ARRAYDATA = new Object[0];

	public CycleStack() {
		this.arrayData = new Object[DEFAULT_CAPACITY];
	}

	public CycleStack(int initialCapacity) {
		if (initialCapacity > 0) {
			this.arrayData = new Object[initialCapacity];
		} else if (initialCapacity == 0) {
			this.arrayData = EMPTY_ARRAYDATA;
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
	}

	public void setCapacity(int newCapacity) {
		checkForComodification();
		if (newCapacity > 0) {
			if (newCapacity < size) {
				throw new IllegalArgumentException("new capacity must be equal to or greater than current size");
			}
			Object[] newArray = new Object[newCapacity];
			for (int i = takePointer, j = 0; j < size; i++, j++) {
				if (i == arrayData.length)
					i = 0;
				newArray[j] = arrayData[i];
			}
			takePointer = 0;
			addPointer = size;
			this.arrayData = newArray;
		} else {
			throw new IllegalArgumentException("Illegal Capacity: " + newCapacity);
		}
	}

	public int getCapacity() {
		return arrayData.length;
	}

	// Checks for concurrent modification.
	private void checkForComodification() {
		if (CycleStack.this.modCount != this.modCount)
			throw new ConcurrentModificationException();
	}

	@SuppressWarnings("unchecked")
	public T get(int index) {
		modCount++;
		if (index > size)
			throw new ArrayIndexOutOfBoundsException(
					"ArrayIndexOutOfBoundsException: " + index + ", max possible: " + size);
		try {
			checkForComodification();
			return (T) arrayData[(takePointer + index) % arrayData.length];
		} catch (ClassCastException e) {
			System.err.println("ClassCastException in RotationalArray.get(int index)");
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public T removeNext() {
		modCount++;
		takePointer %= arrayData.length;
		size--;
		if (arrayData[takePointer] == null) {
			if (!hasAvailable()) {
				if (takePointer != addPointer) {
					System.err.println("Probably has been overtaken, contact developer.");
				}
				return null;
			}
		}
		T toReturn = null;
		try {
			toReturn = (T) arrayData[takePointer];
		} catch (ClassCastException e) {
			System.err.println("ClassCastException in RotationalArray.removeNext()");
			throw e;
		}
		checkForComodification();
		arrayData[takePointer++] = null;
		return toReturn;
	}

	public boolean hasAvailable() {
		for (Object o : arrayData) {
			if (o != null) {
				return true;
			}
		}
		return false;
	}

	public void prepare(int newMemberLength) {
		int increase = newMemberLength - (arrayData.length - size);
		if (increase > 0)
			setCapacity(increase + arrayData.length);
	}

	public void add(T t) {
		modCount++;
		addPointer %= arrayData.length;
		takePointer %= arrayData.length;
		if (addPointer == takePointer && size > 0) {
			setCapacity(size + 1);
		}
		checkForComodification();
		size++;
		arrayData[addPointer++] = t;
	}

	public int size() {
		return size;
	}

	public void clear() {
		modCount++;
		for (int i = 0; i < arrayData.length; i++) {
			arrayData[i] = null;
		}
		size = 0;
	}
}

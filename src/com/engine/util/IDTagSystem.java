package com.engine.util;

public final class IDTagSystem {
	public final class IDTag {
		private final int id;

		private IDTag(final int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public void returnTag() {
			IDTagSystem.this.returnTag(this);
		}
	}

	private final CycleList<IDTag> tags = new CycleList<IDTag>();

	public IDTagSystem(final int maxTags) {
		tags.prepare(maxTags);
		for (int i = 0; i < maxTags; i++) {
			tags.add(new IDTag(i));
		}
	}

	public IDTag getTag() {
		return tags.removeNext();
	}

	private void returnTag(IDTag tag) {
		tags.add(tag);
	}
}

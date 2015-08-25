package org.jalfrezi.datamodel;

import org.jalfrezi.datamodel.id.IndexId;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Index {
	private IndexId indexId;
	private String name;

	public Index() {
	}

	public IndexId getIndexId() {
		return indexId;
	}

	public Index setIndexId(IndexId indexId) {
		this.indexId = indexId;
		return this;
	}

	public String getName() {
		return name;
	}

	public Index setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(indexId, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Index that = (Index) obj;
		return Objects.equal(this.indexId, that.indexId)
				&& Objects.equal(this.name, that.name);
	}

	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("indexId", indexId)
				.add("name", name)
				.toString();
	}
}

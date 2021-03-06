package org.jalfrezi.datamodel;

import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;
import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Share {
	private IndexId indexId;
	private ShareId shareId;
	private String name;
	private DateTime lastFetch;

	public Share() {
	}

	public IndexId getIndexId() {
		return indexId;
	}

	public Share setIndexId(IndexId indexId) {
		this.indexId = indexId;
		return this;
	}

	public ShareId getShareId() {
		return shareId;
	}

	public Share setShareId(ShareId shareId) {
		this.shareId = shareId;
		return this;
	}

	public String getName() {
		return name;
	}

	public Share setName(String name) {
		this.name = name;
		return this;
	}

	public DateTime getLastFetch() {
		return lastFetch;
	}

	public Share setLastFetch(DateTime lastFetch) {
		this.lastFetch = lastFetch;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(indexId, shareId, name, lastFetch);
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
		Share that = (Share) obj;
		return Objects.equal(this.indexId, that.indexId)
				&& Objects.equal(this.shareId, that.shareId)
				&& Objects.equal(this.name, that.name)
				&& Objects.equal(this.lastFetch, that.lastFetch);
	}

	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
	            .add("indexId", indexId)
	            .add("shareId", shareId)
	            .add("name", name)
	            .add("lastFetch", lastFetch)
	            .toString();
	}
}

package org.jalfrezi.datamodel;

import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;

import com.google.common.base.MoreObjects;

public class Share {
	private IndexId indexId;
	private ShareId shareId;
	private String name;

	public Share() {
	}

	public IndexId getIndexId() {
		return indexId;
	}

	public void setIndexId(IndexId indexId) {
		this.indexId = indexId;
	}

	public ShareId getShareId() {
		return shareId;
	}

	public void setShareId(ShareId shareId) {
		this.shareId = shareId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
	            .add("indexId", indexId)
	            .add("shareId", shareId)
	            .add("name", name)
	            .toString();
	}
}

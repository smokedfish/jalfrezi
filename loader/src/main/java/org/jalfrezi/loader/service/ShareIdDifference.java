package org.jalfrezi.loader.service;

import java.util.Set;

import org.jalfrezi.datamodel.id.ShareId;

import com.google.common.collect.Sets;

public class ShareIdDifference {
	private final Set<ShareId> shareIdsInIx;
	private final Set<ShareId> shareIdsInDb;

	public ShareIdDifference(Set<ShareId> shareIdsInIx, Set<ShareId> shareIdsInDb) {
		this.shareIdsInDb = shareIdsInDb;
		this.shareIdsInIx = shareIdsInIx;
	}

	public Set<ShareId> getAddToIndex() {
		return Sets.difference(shareIdsInIx, shareIdsInDb);
	}

	public Set<ShareId> getInIndex() {
		return Sets.intersection(shareIdsInIx, shareIdsInDb);
	}

	public Set<ShareId> getRemoveFromIndex() {
		return Sets.difference(shareIdsInDb, shareIdsInIx);
	}
}

package org.jalfrezi.datamodel.id;

public class IndexId extends Id<String> {

	public static IndexId FTSE100 = new IndexId("^FTSE");
	public static IndexId FTSE250 = new IndexId("^FTMC");

	public IndexId(String symbol) {
		super(symbol);
	}
}

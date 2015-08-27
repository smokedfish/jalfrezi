package org.jalfrezi.loader.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LoaderMain {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = null;
		try {
			ctx = new AnnotationConfigApplicationContext("org.jalfrezi.dao", "org.jalfrezi.yahoo_client", "org.jalfrezi.loader");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			ctx.close();
		}
	}
}

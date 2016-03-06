package org.jalfrezi.server;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

public class ServerMain {

    private static final URI BASE_URI = URI.create("http://localhost:8080/base/");

    public static void main(String[] args) {
        try {
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new JerseyConfig(), false);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.shutdownNow();
                }
            }));
            server.start();
            Thread.currentThread().join();
        }
        catch (IOException | InterruptedException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

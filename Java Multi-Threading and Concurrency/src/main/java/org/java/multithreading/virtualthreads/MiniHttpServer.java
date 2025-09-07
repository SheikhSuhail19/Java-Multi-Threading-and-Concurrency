package org.java.multithreading.virtualthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;


public class MiniHttpServer {
	public static void main(String[] args) throws Exception {
		try (var server = new ServerSocket(8080);
		     var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			System.out.println("Server running on port 8080...");

			while (true) {
				Socket socket = server.accept();
				executor.submit(() -> handle(socket));
			}
		}
	}

	private static void handle(Socket socket) {
		try (socket;
		     var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		     var out = new PrintWriter(socket.getOutputStream(), true)) {

			String line = in.readLine();
			out.println("HTTP/1.1 200 OK\r\nContent-Length: 12\r\n\r\nHello World!");
			System.out.println("Handled request: " + line + " by " + Thread.currentThread());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

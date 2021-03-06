package org.deepserver.td15;

public class Main {

	public static boolean isServer = false;
	public static boolean isClient = true;

	public static boolean isWindowed = false;
	public static boolean isLeft = false;

	public static String serverIP = "127.0.0.1";

	public static void main(String[] args) {
		if (args[0].equals("server")) {
			Main.isServer = true;
			Main.isClient = false;
		}

		if (args[1].equals("windowed"))
			Main.isWindowed = true;
		if (args[2].equals("left"))
			Main.isLeft = true;

		if (args.length > 3)
			serverIP = args[3];

		if (Main.isServer)
			new Server().run();
		else
			new Client().run();
	}

}
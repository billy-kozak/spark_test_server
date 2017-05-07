package spark_test_server;

import spark.Spark;

class Server implements Runnable {

	private static final String HTML_ROOT = "/html";

	public Server() {
	}

	@Override
	public void run() {
		Spark.staticFiles.location(HTML_ROOT);

		Spark.init();
	}
}

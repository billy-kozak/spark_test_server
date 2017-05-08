package spark_test_server;

import java.util.concurrent.Semaphore;

public class Main
{
	private static final Semaphore shutdown_flag = new Semaphore(0, true);
	private static final Thread main_thread = Thread.currentThread();

	private static final Thread shutdown_handler = new Thread() {
		@Override
		public void run() {
			request_shutdown();
			try {
				main_thread.join();
			} catch (InterruptedException e) {
				/* do nothing */
			}
		}
	};

	private static void add_shutdown_hooks() {
		Runtime.getRuntime().addShutdownHook(shutdown_handler);
	}

	public static void request_shutdown() {
		shutdown_flag.release();
	}

    public static void main( String[] args ) {

    	add_shutdown_hooks();

    	Server server = new Server();

    	server.run();

    	try {
			shutdown_flag.acquire();
		} catch (InterruptedException e) {
			/* do nothing */
		}

    	server.stop();
    }
}
/***************************************************************************************************
* Copyright 2017 Bill Kozak                                                                        *
*                                                                                                  *
* Licensed under the Apache License, Version 2.0 (the "License");                                  *
* you may not use this file except in compliance with the License.                                 *
* You may obtain a copy of the License at                                                          *
*                                                                                                  *
*     http://www.apache.org/licenses/LICENSE-2.0                                                   *
*                                                                                                  *
* Unless required by applicable law or agreed to in writing, software                              *
* distributed under the License is distributed on an "AS IS" BASIS,                                *
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                         *
* See the License for the specific language governing permissions and                              *
* limitations under the License.                                                                   *
***************************************************************************************************/

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
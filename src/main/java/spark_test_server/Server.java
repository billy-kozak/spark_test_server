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

	public void stop() {
		Spark.stop();
	}
}

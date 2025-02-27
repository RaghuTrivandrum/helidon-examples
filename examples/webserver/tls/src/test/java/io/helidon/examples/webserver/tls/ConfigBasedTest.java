/*
 * Copyright (c) 2020, 2024 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.examples.webserver.tls;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.webserver.testing.junit5.SetUpServer;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;

class ConfigBasedTest extends TestBase {

    ConfigBasedTest(WebServer server) {
        super(server);
    }

    @SetUpServer
    static void setup(WebServerConfig.Builder server) {
        Config config = Config.create(ConfigSources.classpath("test-application.yaml"),
                ConfigSources.classpath("application.yaml"));
        Main.setupConfigBased(server, config.get("config-based"));
    }
}

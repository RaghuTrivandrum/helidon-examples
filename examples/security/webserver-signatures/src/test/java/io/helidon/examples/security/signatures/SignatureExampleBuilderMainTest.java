/*
 * Copyright (c) 2018, 2024 Oracle and/or its affiliates.
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

package io.helidon.examples.security.signatures;

import java.net.URI;

import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.testing.junit5.ServerTest;
import io.helidon.webserver.testing.junit5.SetUpServer;

/**
 * Unit test for {@link SignatureExampleBuilderMain}.
 */
@ServerTest
public class SignatureExampleBuilderMainTest extends SignatureExampleTest {

    protected SignatureExampleBuilderMainTest(WebServer server, URI uri) {
        super(server, uri);
    }

    @SetUpServer
    public static void setup(WebServerConfig.Builder server) {
        SignatureExampleBuilderMain.setup(server);
    }
}

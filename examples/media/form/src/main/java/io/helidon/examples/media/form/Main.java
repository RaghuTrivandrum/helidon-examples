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
package io.helidon.examples.media.form;

import io.helidon.http.Header;
import io.helidon.http.HeaderNames;
import io.helidon.http.HeaderValues;
import io.helidon.http.Status;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.staticcontent.StaticContentService;

/**
 * This application provides a simple service with a UI to exercise forms.
 */
public final class Main {
    private static final Header UI_LOCATION = HeaderValues.createCached(HeaderNames.LOCATION, "/ui");

    private Main() {
    }

    /**
     * Executes the example.
     *
     * @param args command line arguments, ignored
     */
    public static void main(String[] args) {
        // load logging configuration
        LogConfig.configureRuntime();

        WebServer server = WebServer.builder()
                .routing(Main::routing)
                .port(8080)
                .build()
                .start();

        System.out.println("WEB server is up! http://localhost:" + server.port());
    }

    /**
     * Updates the routing rules.
     *
     * @param rules routing rules
     */
    static void routing(HttpRules rules) {
        rules.any("/", (req, res) -> {
                    res.status(Status.MOVED_PERMANENTLY_301);
                    res.header(UI_LOCATION);
                    res.send();
                })
                .register("/ui", StaticContentService.builder("WEB")
                        .welcomeFileName("index.html")
                        .build())
                .register("/api", new FormService());
    }
}

/*
 * Copyright (c) 2023, 2024 Oracle and/or its affiliates.
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

package io.helidon.examples.webserver.staticcontent;

import io.helidon.http.Status;
import io.helidon.webclient.http1.Http1Client;
import io.helidon.webclient.http1.Http1ClientResponse;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.testing.junit5.ServerTest;
import io.helidon.webserver.testing.junit5.SetUpRoute;

import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ServerTest
class MainTest {

    private final Http1Client client;

    protected MainTest(Http1Client client) {
        this.client = client;
    }

    @SetUpRoute
    static void routing(HttpRouting.Builder builder) {
        Main.routing(builder);
    }

    @Test
    void testUi() {
        assertThat(allCounter(), is(1));
        try (Http1ClientResponse response = client.get("/ui/index.html").request()) {
            assertThat(response.status(), is(Status.OK_200));
            assertThat(response.headers().contentType().orElseThrow().text(), is("text/html"));
        }
        try (Http1ClientResponse response = client.get("/ui/css/app.css").request()) {
            assertThat(response.status(), is(Status.OK_200));
            assertThat(response.headers().contentType().orElseThrow().text(), is("text/css"));
        }
        try (Http1ClientResponse response = client.get("/ui/js/app.js").request()) {
            assertThat(response.status(), is(Status.OK_200));
            assertThat(response.headers().contentType().orElseThrow().text(), is("text/javascript"));
        }
        assertThat(allCounter(), is(5));        // includes /ui/api/counter calls
    }

    private int allCounter() {
        try (Http1ClientResponse response = client.get("/ui/api/counter").request()) {
            assertThat(response.status(), is(Status.OK_200));
            JsonNumber number = (JsonNumber) response.as(JsonObject.class).get("all");
            return number.intValue();
        }
    }
}

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

package io.helidon.microprofile.example.helloworld.explicit;

import java.net.URI;
import java.util.Collections;

import io.helidon.config.Config;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Example resource.
 */
@Path("helloworld")
@RequestScoped
public class HelloWorldResource {
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private final Config config;
    private final String applicationName;
    private final URI applicationUri;
    private BeanManager beanManager;

    /**
     * Constructor injection of field values.
     *
     * @param config      configuration instance
     * @param appName     name of application from config (app.name)
     * @param appUri      URI of application from config (app.uri)
     * @param beanManager bean manager (injected automatically by CDI)
     */
    @Inject
    public HelloWorldResource(Config config,
                              @ConfigProperty(name = "app.name") String appName,
                              @ConfigProperty(name = "app.uri") URI appUri,
                              BeanManager beanManager) {
        this.config = config;
        this.applicationName = appName;
        this.applicationUri = appUri;
        this.beanManager = beanManager;
    }

    /**
     * Hello world GET method.
     *
     * @return string with application name
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        return "Hello World from application " + applicationName;
    }

    /**
     * Hello World GET method returning JSON.
     *
     * @param name name to add to response
     * @return JSON with name and configured fields of this class
     */
    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getHello(@PathParam("name") String name) {
        return JSON.createObjectBuilder()
                .add("name", name)
                .add("appName", applicationName)
                .add("appUri", String.valueOf(applicationUri))
                .add("config", config.get("my.property").asString().get())
                .add("beanManager", beanManager.toString())
                .build();
    }
}

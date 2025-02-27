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

package io.helidon.examples.messaging.se;

import io.helidon.config.Config;
import io.helidon.messaging.Channel;
import io.helidon.messaging.Emitter;
import io.helidon.messaging.Messaging;
import io.helidon.messaging.connectors.kafka.KafkaConnector;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;

import org.apache.kafka.common.serialization.StringSerializer;

class SendingService implements HttpService {

    private final Emitter<String> emitter;
    private final Messaging messaging;

    SendingService(Config config) {

        String kafkaServer = config.get("app.kafka.bootstrap.servers").asString().get();
        String topic = config.get("app.kafka.topic").asString().get();
        String compression = config.get("app.kafka.compression").asString().orElse("none");

        // Prepare channel for connecting processor -> kafka connector with specific subscriber configuration,
        // channel -> connector mapping is automatic when using KafkaConnector.configBuilder()
        Channel<String> toKafka = Channel.<String>builder()
                                         .subscriberConfig(KafkaConnector.configBuilder()
                                                                         .bootstrapServers(kafkaServer)
                                                                         .topic(topic)
                                                                         .compressionType(compression)
                                                                         .keySerializer(StringSerializer.class)
                                                                         .valueSerializer(StringSerializer.class)
                                                                         .build())
                                         .build();

        // Prepare channel for connecting emitter -> processor
        Channel<String> toProcessor = Channel.create();

        // Prepare Kafka connector, can be used by any channel
        KafkaConnector kafkaConnector = KafkaConnector.create();

        // Prepare emitter for manual publishing to channel
        emitter = Emitter.create(toProcessor);

        // Transforming to upper-case before sending to kafka
        messaging = Messaging.builder()
                .emitter(emitter)
                // Processor connect two channels together
                .processor(toProcessor, toKafka, String::toUpperCase)
                .connector(kafkaConnector)
                .build()
                .start();
    }

    /**
     * A service registers itself by updating the routing rules.
     *
     * @param rules the routing rules.
     */
    @Override
    public void routing(HttpRules rules) {
        // Listen for GET /example/send/{msg}
        // to send it thru messaging to Kafka
        rules.get("/send/{msg}", (req, res) -> {
            String msg = req.path().pathParameters().get("msg");
            System.out.println("Emitting: " + msg);
            emitter.send(msg);
            res.send();
        });
    }

    /**
     * Gracefully terminate messaging.
     */
    public void shutdown() {
        messaging.stop();
    }
}

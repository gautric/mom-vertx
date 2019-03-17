package net.a.g.vertx.mom.mqtt.cb;

import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.HOST;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.PORT;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.TOPIC;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.messages.MqttPublishMessage;

public class MQTTReceiverVerticle extends AbstractVerticle {

	MqttClient client = null;

	@Override
	public void start() {

		client = MqttClient.create(vertx);

		client.publishHandler(handler());

		client.connect(PORT, HOST, ca -> {
			client.subscribe(TOPIC, 0);
		});

	}

	public Handler<MqttPublishMessage> handler() {
		return msg -> {
			System.out.println("Topic: " + msg.topicName());
			System.out.println("Message: " + msg.payload().toString());
			System.out.println("QoS: " + msg.qosLevel());
		};
	}

	@Override
	public void stop() throws Exception {
		client.disconnect();
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(MQTTReceiverVerticle.class.getCanonicalName());
	}
}
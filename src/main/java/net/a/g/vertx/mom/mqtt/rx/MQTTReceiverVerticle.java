package net.a.g.vertx.mom.mqtt.rx;

import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.HOST;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.PORT;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.TOPIC;

import io.vertx.core.Handler;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.mqtt.MqttClient;
import io.vertx.reactivex.mqtt.messages.MqttPublishMessage;

public class MQTTReceiverVerticle extends AbstractVerticle {

	MqttClient client = null;

	@Override
	public void start() {
		client = MqttClient.create(vertx);

		client.rxConnect(PORT, HOST).flatMap(x -> client.rxSubscribe(TOPIC, 0)).subscribe(succ -> {
			client.publishHandler(handler());
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
		client.rxDisconnect().subscribe();
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(MQTTReceiverVerticle.class.getCanonicalName());

	}
}
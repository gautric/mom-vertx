package net.a.g.vertx.mqtt.cb;

import static net.a.g.vertx.mqtt.util.MQTTConstantes.HOST;
import static net.a.g.vertx.mqtt.util.MQTTConstantes.PORT;
import static net.a.g.vertx.mqtt.util.MQTTConstantes.TOPIC;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class MQTTSenderVerticle extends AbstractVerticle {

	MqttClient client = null;

	@Override
	public void start() {

		MqttClientOptions options = new MqttClientOptions();

		client = MqttClient.create(vertx, options);

		client.connect(PORT, HOST, s -> {
			vertx.setPeriodic(5000, id -> {
				client.publish(TOPIC, Buffer.buffer("Hello"), MqttQoS.AT_LEAST_ONCE, true, true);
			});
		});
	}

	@Override
	public void stop() throws Exception {
		client.disconnect(disconnectHandler -> {
			if (disconnectHandler.failed()) {
				System.err.println("Stop Failed");
			}
		});
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(MQTTSenderVerticle.class.getCanonicalName());
	}
}
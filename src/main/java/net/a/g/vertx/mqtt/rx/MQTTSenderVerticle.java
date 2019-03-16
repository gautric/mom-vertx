package net.a.g.vertx.mqtt.rx;

import static net.a.g.vertx.mqtt.util.MQTTConstantes.HOST;
import static net.a.g.vertx.mqtt.util.MQTTConstantes.PORT;
import static net.a.g.vertx.mqtt.util.MQTTConstantes.TOPIC;

import java.util.concurrent.TimeUnit;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.reactivex.Flowable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.mqtt.MqttClient;

// https://www.programcreek.com/java-api-examples/?code=cescoffier/various-vertx-demos/various-vertx-demos-master/iot-gateway/src/main/java/me/escoffier/demo/iot/Sensor.java#
public class MQTTSenderVerticle extends AbstractVerticle {

	MqttClient client = null;

	@Override
	public void start() {
		client = MqttClient.create(vertx);

		client.rxConnect(PORT, HOST).flatMapPublisher(
				ack -> Flowable.interval(1, TimeUnit.SECONDS).filter(it -> it % 2 == 0).flatMapSingle(it -> {
					return client.rxPublish(TOPIC, Buffer.buffer("Counter : " + it), MqttQoS.AT_MOST_ONCE, false,
							false);
				})).subscribe();
	}

	@Override
	public void stop() throws Exception {
		client.rxDisconnect().subscribe();
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(MQTTSenderVerticle.class.getCanonicalName());

	}
}
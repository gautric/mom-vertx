package net.a.g.vertx.mom.mqtt.rx;

import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.VERTX_RX_CLIENT_SENDER;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.HOST;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.PORT;
import static net.a.g.vertx.mom.mqtt.util.MQTTConstantes.TOPIC;

import java.util.concurrent.TimeUnit;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.reactivex.Flowable;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.mqtt.MqttClient;

public class MQTTSenderVerticle extends AbstractVerticle {

	MqttClient client = null;

	@Override
	public void start() {
		MqttClientOptions options = new MqttClientOptions();
		options.setClientId(VERTX_RX_CLIENT_SENDER);

		client = MqttClient.create(vertx, options);

		client.rxConnect(PORT, HOST)
				.flatMapPublisher(ack -> Flowable.interval(1, TimeUnit.SECONDS).flatMapSingle(it -> {
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
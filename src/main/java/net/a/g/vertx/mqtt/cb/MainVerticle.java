package net.a.g.vertx.mqtt.cb;

import io.vertx.core.Vertx;

public class MainVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(MQTTSenderVerticle.class.getCanonicalName());
		vertx.deployVerticle(MQTTReceiverVerticle.class.getCanonicalName());
	}
}
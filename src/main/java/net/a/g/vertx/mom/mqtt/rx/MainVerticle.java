package net.a.g.vertx.mom.mqtt.rx;

import io.vertx.reactivex.core.Vertx;

public class MainVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();

		vertx.rxDeployVerticle(MQTTSenderVerticle.class.getCanonicalName())
				.flatMap(x -> vertx.rxDeployVerticle(MQTTReceiverVerticle.class.getCanonicalName())).subscribe();

	}
}
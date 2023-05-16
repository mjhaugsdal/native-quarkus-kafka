package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletableFuture;

@Path("/prices")
public class PriceResource {

    @Inject
    @Channel("prices")
    Emitter<Double> priceEmitter;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void addPrice(Double price) {
        priceEmitter.send(Message.of(price)
                .withAck(() -> {
                    // Called when the message is acked
                    return CompletableFuture.completedFuture(null);
                })
                .withNack(throwable -> {
                    // Called when the message is nacked
                    return CompletableFuture.completedFuture(null);
                }));
    }
}
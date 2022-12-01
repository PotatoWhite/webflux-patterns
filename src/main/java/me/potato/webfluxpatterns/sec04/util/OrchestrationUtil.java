package me.potato.webfluxpatterns.sec04.util;

import me.potato.webfluxpatterns.sec04.dto.InventoryRequest;
import me.potato.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import me.potato.webfluxpatterns.sec04.dto.PaymentRequest;
import me.potato.webfluxpatterns.sec04.dto.ShippingRequest;

public class OrchestrationUtil {

    public static void buildPaymentRequest(OrchestrationRequestContext ctx) {
        var paymentRequest = PaymentRequest.of(
                ctx.getOrderRequest().getUserId()
                , ctx.getProductPrice() * ctx.getOrderRequest().getQuantity()
                , ctx.getOrderId());

        ctx.setPaymentRequest(paymentRequest);
    }

    public static void buildInventoryRequest(OrchestrationRequestContext ctx) {
        var inventoryRequest = InventoryRequest.of(
                ctx.getPaymentResponse().getPaymentId()
                , ctx.getOrderRequest().getProductId()
                , ctx.getOrderRequest().getQuantity());

        ctx.setInventoryRequest(inventoryRequest);
    }

    public static void buildShippingRequest(OrchestrationRequestContext ctx) {
        var shippingRequest = ShippingRequest.of(
                ctx.getOrderRequest().getQuantity()
                , ctx.getOrderRequest().getUserId()
                , ctx.getInventoryResponse().getInventoryId());

        ctx.setShippingRequest(shippingRequest);
    }

}

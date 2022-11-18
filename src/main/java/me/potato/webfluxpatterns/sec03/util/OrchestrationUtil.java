package me.potato.webfluxpatterns.sec03.util;

import me.potato.webfluxpatterns.sec03.dto.InventoryRequest;
import me.potato.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import me.potato.webfluxpatterns.sec03.dto.PaymentRequest;
import me.potato.webfluxpatterns.sec03.dto.ShippingRequest;

public class OrchestrationUtil {
    public static void buildRequestContext(OrchestrationRequestContext ctx) {
        buildPaymentRequest(ctx);
        buildInventoryRequest(ctx);
        buildShippingRequest(ctx);
    }

    private static void buildPaymentRequest(OrchestrationRequestContext ctx) {
        var paymentRequest = PaymentRequest.of(
                ctx.getOrderRequest().getUserId()
                , ctx.getProductPrice() * ctx.getOrderRequest().getQuantity()
                , ctx.getOrderId());

        ctx.setPaymentRequest(paymentRequest);
    }

    private static void buildInventoryRequest(OrchestrationRequestContext ctx) {
        var inventoryRequest = InventoryRequest.of(
                ctx.getOrderId()
                , ctx.getOrderRequest().getProductId()
                , ctx.getOrderRequest().getQuantity());

        ctx.setInventoryRequest(inventoryRequest);
    }

    private static void buildShippingRequest(OrchestrationRequestContext ctx) {
        var shippingRequest = ShippingRequest.of(
                ctx.getOrderRequest().getQuantity()
                , ctx.getOrderRequest().getUserId()
                , ctx.getOrderId());

        ctx.setShippingRequest(shippingRequest);
    }

}

package me.potato.webfluxpatterns.sec03.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.potato.webfluxpatterns.sec03.dto.OrchestrationRequestContext;

public class DebugUtil {
    public static void print(OrchestrationRequestContext ctx) {
        var mapper = new ObjectMapper();
        try {
            System.out.println("------------------------");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ctx));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

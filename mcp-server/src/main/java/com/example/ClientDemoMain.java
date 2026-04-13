package com.example;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;

public class ClientDemoMain {
    public static void main(String[] args) {
        var clientTransport = HttpClientStreamableHttpTransport
                .builder("http://localhost:8081")
                .endpoint("/mcp")
                .build();

        McpSyncClient client = McpClient.sync(clientTransport)
                .build();

        client.initialize();
        // client.listTools().tools().forEach(System.out::println);
        client.callTool(McpSchema.CallToolRequest.builder()
                        .name("Test")
                        .build())
                .content()
                .forEach(System.out::println);
    }
}

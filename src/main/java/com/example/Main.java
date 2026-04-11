package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello and welcome!");

        var transportProvider = HttpServletStreamableServerTransportProvider.builder()
                .mcpEndpoint("/mcp")
                .build();

        McpSchema.Tool bioSensorTool = McpSchema.Tool.builder()
                .name("Test")
                .title("Human Vital Pulse Sensor")
                .description("Returns the current heart rate of the user as a simple string value")
                .inputSchema(new JacksonMcpJsonMapper(new ObjectMapper()), createBioSensorSchema())
                .build();

        McpServerFeatures.SyncToolSpecification bioSensorToolSpec = McpServerFeatures.SyncToolSpecification.builder()
                .tool(bioSensorTool)
                .callHandler((mcpSyncServerExchange, callToolRequest) ->
                        new McpSchema.CallToolResult("Пульс 42", false))
                .build();

        McpServer.sync(transportProvider)
                .serverInfo("MCP Server", "1.0")
                .capabilities(createServerCapabilities())
                .tools(bioSensorToolSpec)
                .build();

        Server server = new Server(8081);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(transportProvider), "/mcp");
        server.setHandler(contextHandler);
        server.start();
        server.join();
    }

    private static String createBioSensorSchema() {
        return new ObjectMapper().createObjectNode().put("type", "object").toString();
    }

    private static McpSchema.ServerCapabilities createServerCapabilities() {
        return McpSchema.ServerCapabilities.builder()
                .tools(true)
                .build();
    }
}

package com.example;

import io.modelcontextprotocol.server.McpServer;
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
        McpServer.sync(transportProvider)
                .capabilities(createServerCapabilities())
                .build();

        Server server = new Server(8081);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(transportProvider), "/mcp");
        server.setHandler(contextHandler);
        server.start();
        server.join();
    }

    private static McpSchema.ServerCapabilities createServerCapabilities() {
        return McpSchema.ServerCapabilities.builder().build();
    }
}

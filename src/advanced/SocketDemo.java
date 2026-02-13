package advanced;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * Socket网络编程详解
 * 学习目标：
 * 1. 理解TCP/IP通信原理
 * 2. 掌握Socket客户端和服务端编程
 * 3. 学习多线程处理并发连接
 * 4. 了解网络通信的实际应用
 *
 * Socket是什么？
 * - 网络通信的端点（EndPoint）
 * - 基于TCP/IP协议的双向通信
 * - Java提供Socket（客户端）和ServerSocket（服务端）
 * - 是HTTP、WebSocket等高级协议的基础
 *
 * Spring应用：
 * - Tomcat底层网络通信
 * - Spring WebSocket
 * - 微服务间RPC通信
 * - 分布式系统数据传输
 */
public class SocketDemo {

    private static final int PORT = 8888;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Socket网络编程详解 ===\n");

        demonstrateBasicSocket();
        demonstrateMultiClientServer();
        demonstrateURLConnection();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. 基础Socket通信
     */
    private static void demonstrateBasicSocket() throws Exception {
        System.out.println("1. 基础Socket通信\n");

        // 启动服务端（在新线程中）
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                System.out.println("  [服务端] 启动成功，监听端口: " + PORT);

                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("  [服务端] 客户端已连接: " + 
                    clientSocket.getInetAddress().getHostAddress());

                // 读取客户端消息
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                System.out.println("  [服务端] 收到消息: " + message);

                // 发送响应
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Hello, Client! 服务器收到: " + message);

                // 关闭连接
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // 等待服务端启动
        Thread.sleep(1000);

        // 启动客户端
        System.out.println("\n  [客户端] 连接服务器...");
        Socket socket = new Socket(HOST, PORT);
        System.out.println("  [客户端] 已连接到服务器");

        // 发送消息
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Hello, Server!");
        System.out.println("  [客户端] 已发送消息");

        // 接收响应
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        System.out.println("  [客户端] 收到响应: " + response);

        // 关闭连接
        socket.close();
        serverThread.join();

        System.out.println("\n基础Socket通信完成\n");
    }

    /**
     * 2. 多客户端并发服务器
     */
    private static void demonstrateMultiClientServer() throws Exception {
        System.out.println("2. 多客户端并发服务器\n");

        final int serverPort = 8889;

        // 启动多线程服务器
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(serverPort);
                System.out.println("  [服务器] 多线程服务器启动，端口: " + serverPort);

                ExecutorService executor = Executors.newFixedThreadPool(5);

                // 接受3个客户端连接
                for (int i = 0; i < 3; i++) {
                    Socket clientSocket = serverSocket.accept();
                    final int clientId = i + 1;

                    // 为每个客户端创建处理线程
                    executor.submit(() -> handleClient(clientSocket, clientId));
                }

                Thread.sleep(3000); // 等待所有客户端处理完成
                executor.shutdown();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // 等待服务器启动
        Thread.sleep(1000);

        // 启动3个客户端
        ExecutorService clientExecutor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            final int clientNum = i;
            clientExecutor.submit(() -> {
                try {
                    Socket socket = new Socket(HOST, serverPort);
                    
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Client-" + clientNum + " 的消息");

                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                    String response = in.readLine();
                    System.out.println("  [客户端" + clientNum + "] 收到: " + response);

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        clientExecutor.shutdown();
        clientExecutor.awaitTermination(5, TimeUnit.SECONDS);
        serverThread.join();

        System.out.println("\n多客户端并发处理完成\n");
    }

    /**
     * 3. URL连接（HTTP通信）
     */
    private static void demonstrateURLConnection() throws Exception {
        System.out.println("3. URL连接（HTTP通信）\n");

        // 使用URLConnection获取网页内容
        System.out.println("模拟HTTP GET请求:");
        
        // 注意：这里使用一个稳定的API
        String urlString = "https://httpbin.org/get";
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法和属性
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java-SocketDemo");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 获取响应码
            int responseCode = connection.getResponseCode();
            System.out.println("  响应码: " + responseCode);

            if (responseCode == 200) {
                // 读取响应内容
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                
                String line;
                int lineCount = 0;
                System.out.println("  响应内容（前5行）:");
                while ((line = reader.readLine()) != null && lineCount < 5) {
                    System.out.println("    " + line);
                    lineCount++;
                }
                reader.close();
            }

            connection.disconnect();
        } catch (Exception e) {
            System.out.println("  网络请求失败（可能需要网络连接）: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * 4. 实际应用场景
     */
    private static void demonstrateRealWorldExamples() throws Exception {
        System.out.println("4. 实际应用场景\n");

        System.out.println("场景1: 简单的Echo服务器（回声服务）");
        demonstrateEchoServer();

        System.out.println("\n场景2: 心跳检测机制");
        demonstrateHeartbeat();

        System.out.println("\n场景3: Socket超时处理");
        demonstrateSocketTimeout();
    }

    /**
     * Echo服务器实现
     */
    private static void demonstrateEchoServer() throws Exception {
        final int echoPort = 8890;

        // 启动Echo服务器
        Thread server = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(echoPort);
                serverSocket.setSoTimeout(3000); // 3秒超时

                Socket client = serverSocket.accept();
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("  [Echo服务器] 收到: " + line);
                    out.println("Echo: " + line); // 回声
                    if ("bye".equalsIgnoreCase(line)) {
                        break;
                    }
                }

                client.close();
                serverSocket.close();
            } catch (Exception e) {
                // 超时正常
            }
        });
        server.start();

        Thread.sleep(500);

        // 客户端发送多条消息
        Socket socket = new Socket(HOST, echoPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        String[] messages = {"Hello", "World", "bye"};
        for (String msg : messages) {
            out.println(msg);
            String response = in.readLine();
            System.out.println("  [Echo客户端] 收到: " + response);
        }

        socket.close();
        server.join();
    }

    /**
     * 心跳检测
     */
    private static void demonstrateHeartbeat() throws Exception {
        final int heartbeatPort = 8891;

        // 服务端
        Thread server = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(heartbeatPort);
                Socket client = serverSocket.accept();
                
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                int heartbeatCount = 0;
                String line;
                while ((line = in.readLine()) != null && heartbeatCount < 3) {
                    if ("PING".equals(line)) {
                        heartbeatCount++;
                        System.out.println("  [服务器] 收到心跳 #" + heartbeatCount);
                        out.println("PONG");
                    }
                }

                client.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        server.start();

        Thread.sleep(500);

        // 客户端发送心跳
        Socket socket = new Socket(HOST, heartbeatPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        for (int i = 1; i <= 3; i++) {
            out.println("PING");
            String response = in.readLine();
            System.out.println("  [客户端] 发送心跳 #" + i + ", 收到: " + response);
            Thread.sleep(500);
        }

        socket.close();
        server.join();
    }

    /**
     * Socket超时处理
     */
    private static void demonstrateSocketTimeout() throws Exception {
        System.out.println("  演示Socket读取超时:");

        final int timeoutPort = 8892;

        // 服务端（故意不发送数据）
        Thread server = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(timeoutPort);
                Socket client = serverSocket.accept();
                System.out.println("  [服务器] 客户端已连接，等待5秒...");
                Thread.sleep(5000);
                client.close();
                serverSocket.close();
            } catch (Exception e) {
                // ignore
            }
        });
        server.start();

        Thread.sleep(500);

        // 客户端设置超时
        try {
            Socket socket = new Socket(HOST, timeoutPort);
            socket.setSoTimeout(2000); // 2秒读取超时

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            
            System.out.println("  [客户端] 等待数据（2秒超时）...");
            in.readLine(); // 这里会超时
            
        } catch (SocketTimeoutException e) {
            System.out.println("  [客户端] 读取超时！（正常，演示超时处理）");
        }

        server.join();
    }

    // ==================== 辅助方法 ====================

    /**
     * 处理单个客户端连接
     */
    private static void handleClient(Socket clientSocket, int clientId) {
        try {
            System.out.println("  [服务器] 处理客户端 #" + clientId);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message = in.readLine();
            System.out.println("  [服务器] 客户端 #" + clientId + " 说: " + message);

            // 响应
            out.println("服务器收到客户端 #" + clientId + " 的消息: " + message);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

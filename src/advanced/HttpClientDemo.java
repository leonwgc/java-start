package advanced;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP客户端学习
 * 学习目标：
 * 1. 理解HTTP请求和响应
 * 2. 掌握Java HttpClient的使用
 * 3. 学习同步和异步请求
 * 4. 理解HTTP在Spring中的应用
 */
public class HttpClientDemo {
    public static void main(String[] args) {
        System.out.println("=== HTTP客户端学习 ===\n");

        // 示例1：GET请求
        demonstrateGetRequest();

        // 示例2：POST请求
        demonstratePostRequest();

        // 示例3：异步请求
        demonstrateAsyncRequest();

        // 示例4：请求配置
        demonstrateRequestConfiguration();

        // 示例5：实战案例
        demonstratePracticalExample();
    }

    // GET请求
    public static void demonstrateGetRequest() {
        System.out.println("1. GET请求\n");
        System.out.println("作用：向服务器请求数据");
        System.out.println("Spring应用：RestTemplate、WebClient GET请求\n");

        try {
            // 创建HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // 创建请求
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .GET()
                .build();

            // 发送请求
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // 处理响应
            System.out.println("状态码: " + response.statusCode());
            System.out.println("响应体:");
            System.out.println(response.body());

        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
        }
        System.out.println();
    }

    // POST请求
    public static void demonstratePostRequest() {
        System.out.println("2. POST请求\n");
        System.out.println("作用：向服务器提交数据");
        System.out.println("Spring应用：RestTemplate、WebClient POST请求\n");

        try {
            HttpClient client = HttpClient.newHttpClient();

            // JSON数据
            String json = "{\"title\":\"测试文章\",\"body\":\"这是内容\",\"userId\":1}";

            // 创建POST请求
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            // 发送请求
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            System.out.println("状态码: " + response.statusCode());
            System.out.println("响应体:");
            System.out.println(response.body());

        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
        }
        System.out.println();
    }

    // 异步请求
    public static void demonstrateAsyncRequest() {
        System.out.println("3. 异步请求\n");
        System.out.println("作用：非阻塞方式发送HTTP请求");
        System.out.println("Spring应用：WebClient异步请求、Reactive编程\n");

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .build();

            // 异步发送请求
            System.out.println("发送异步请求...");
            CompletableFuture<HttpResponse<String>> future =
                client.sendAsync(request, BodyHandlers.ofString());

            // 处理响应
            future.thenApply(HttpResponse::body)
                  .thenAccept(body -> {
                      System.out.println("异步响应接收:");
                      System.out.println(body.substring(0, Math.min(100, body.length())) + "...");
                  })
                  .join(); // 等待完成

            System.out.println("异步请求完成");

        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
        }
        System.out.println();
    }

    // 请求配置
    public static void demonstrateRequestConfiguration() {
        System.out.println("4. 请求配置\n");
        System.out.println("作用：配置超时、重定向、认证等");
        System.out.println("Spring应用：配置RestTemplate、WebClient行为\n");

        try {
            // 配置HttpClient
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)  // HTTP/2
                .connectTimeout(Duration.ofSeconds(10))  // 连接超时
                .followRedirects(HttpClient.Redirect.NORMAL)  // 跟随重定向
                .build();

            // 配置请求
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .timeout(Duration.ofSeconds(5))  // 请求超时
                .header("User-Agent", "Java HttpClient")
                .header("Accept", "application/json")
                .GET()
                .build();

            System.out.println("请求配置:");
            System.out.println("  HTTP版本: HTTP/2");
            System.out.println("  连接超时: 10秒");
            System.out.println("  请求超时: 5秒");
            System.out.println("  自定义Header: User-Agent, Accept");

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("\n状态码: " + response.statusCode());
            System.out.println("HTTP版本: " + response.version());

        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
        }
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("5. 实战案例 - API客户端封装\n");

        ApiClient apiClient = new ApiClient("https://jsonplaceholder.typicode.com");

        // 获取单个资源
        System.out.println("1. 获取POST 1:");
        try {
            String post = apiClient.get("/posts/1");
            System.out.println(post.substring(0, Math.min(100, post.length())) + "...\n");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }

        // 创建资源
        System.out.println("2. 创建新POST:");
        try {
            String json = "{\"title\":\"New Post\",\"body\":\"Content\",\"userId\":1}";
            String result = apiClient.post("/posts", json);
            System.out.println(result.substring(0, Math.min(100, result.length())) + "...\n");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }

        // 更新资源
        System.out.println("3. 更新POST 1:");
        try {
            String json = "{\"title\":\"Updated\",\"body\":\"New Content\",\"userId\":1}";
            String result = apiClient.put("/posts/1", json);
            System.out.println(result.substring(0, Math.min(100, result.length())) + "...\n");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }

        // 删除资源
        System.out.println("4. 删除POST 1:");
        try {
            apiClient.delete("/posts/1");
            System.out.println("删除成功\n");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }

        System.out.println("💡 Spring中的应用：");
        System.out.println("- RestTemplate - 同步HTTP客户端");
        System.out.println("- WebClient - 异步HTTP客户端（响应式编程）");
        System.out.println("- @FeignClient - 声明式HTTP客户端");
        System.out.println("- 微服务间通信、调用第三方API");
        System.out.println();
    }

    // ========== API客户端类 ==========

    static class ApiClient {
        private final HttpClient client;
        private final String baseUrl;

        public ApiClient(String baseUrl) {
            this.baseUrl = baseUrl;
            this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        }

        public String get(String path) throws Exception {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            checkResponse(response);
            return response.body();
        }

        public String post(String path, String json) throws Exception {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            checkResponse(response);
            return response.body();
        }

        public String put(String path, String json) throws Exception {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            checkResponse(response);
            return response.body();
        }

        public void delete(String path) throws Exception {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .DELETE()
                .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            checkResponse(response);
        }

        private void checkResponse(HttpResponse<String> response) throws Exception {
            if (response.statusCode() >= 400) {
                throw new Exception("HTTP错误: " + response.statusCode());
            }
        }
    }
}

package advanced;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * NIO（New I/O）文件操作演示
 * 学习目标：
 * 1. 理解 NIO 与传统 I/O 的区别（Channel + Buffer vs Stream）
 * 2. 掌握 Path、Files 工具类的使用
 * 3. 理解 FileChannel 和 ByteBuffer 的工作原理
 *
 * Spring应用场景：
 * - Spring Boot 底层使用 NIO 处理网络 I/O（Netty、Tomcat NIO）
 * - 文件上传下载功能的高性能实现
 * - 大文件处理和流式数据传输
 */
public class NioDemo {

    public static void main(String[] args) {
        System.out.println("=== NIO 文件操作学习 ===\n");

        demonstratePathAndFiles();
        demonstrateFileChannel();
        demonstrateDirectoryOperations();
        demonstrateWatchService();
    }

    /**
     * 演示 Path 和 Files 工具类
     * 作用：提供现代化的文件路径和文件操作 API
     * Spring应用：配置文件读取、资源文件管理
     */
    private static void demonstratePathAndFiles() {
        System.out.println("1. Path 和 Files 工具类\n");

        try {
            // 创建临时文件
            Path tempFile = Files.createTempFile("nio-demo-", ".txt");
            System.out.println("临时文件路径：" + tempFile);

            // 写入文件（一次性写入所有行）
            List<String> lines = List.of(
                "第一行：NIO 提供了更高效的文件操作",
                "第二行：支持非阻塞 I/O",
                "第三行：基于 Channel 和 Buffer"
            );
            Files.write(tempFile, lines, StandardCharsets.UTF_8);
            System.out.println("✓ 写入 " + lines.size() + " 行数据");

            // 读取文件（一次性读取所有行）
            List<String> readLines = Files.readAllLines(tempFile, StandardCharsets.UTF_8);
            System.out.println("✓ 读取内容：");
            readLines.forEach(line -> System.out.println("  " + line));

            // 文件属性
            System.out.println("\n文件信息：");
            System.out.println("  大小：" + Files.size(tempFile) + " 字节");
            System.out.println("  可读：" + Files.isReadable(tempFile));
            System.out.println("  可写：" + Files.isWritable(tempFile));
            System.out.println("  隐藏：" + Files.isHidden(tempFile));

            // 删除文件
            Files.deleteIfExists(tempFile);
            System.out.println("✓ 文件已清理\n");

        } catch (IOException e) {
            System.err.println("文件操作失败：" + e.getMessage());
        }
    }

    /**
     * 演示 FileChannel 和 ByteBuffer
     * 作用：基于通道和缓冲区的高性能文件读写
     * Spring应用：理解 Netty、Tomcat NIO Connector 的底层原理
     */
    private static void demonstrateFileChannel() {
        System.out.println("2. FileChannel 和 ByteBuffer\n");

        try {
            Path tempFile = Files.createTempFile("channel-demo-", ".dat");

            // 写入数据
            try (FileChannel channel = FileChannel.open(tempFile,
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

                String data = "NIO Channel + Buffer 机制实现零拷贝";
                ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));

                int bytesWritten = channel.write(buffer);
                System.out.println("✓ 通过 FileChannel 写入 " + bytesWritten + " 字节");
            }

            // 读取数据
            try (FileChannel channel = FileChannel.open(tempFile, StandardOpenOption.READ)) {
                ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());

                int bytesRead = channel.read(buffer);
                buffer.flip(); // 切换到读模式

                String content = StandardCharsets.UTF_8.decode(buffer).toString();
                System.out.println("✓ 通过 FileChannel 读取 " + bytesRead + " 字节");
                System.out.println("  内容：" + content);
            }

            // Buffer 工作原理演示
            System.out.println("\n✓ ByteBuffer 工作原理：");
            ByteBuffer bb = ByteBuffer.allocate(10);
            System.out.println("  初始状态 - position: " + bb.position() +
                             ", limit: " + bb.limit() + ", capacity: " + bb.capacity());

            bb.put("Hello".getBytes());
            System.out.println("  写入后   - position: " + bb.position() +
                             ", limit: " + bb.limit() + ", capacity: " + bb.capacity());

            bb.flip();
            System.out.println("  flip后   - position: " + bb.position() +
                             ", limit: " + bb.limit() + ", capacity: " + bb.capacity());

            Files.deleteIfExists(tempFile);
            System.out.println();

        } catch (IOException e) {
            System.err.println("Channel 操作失败：" + e.getMessage());
        }
    }

    /**
     * 演示目录遍历和文件查找
     * 作用：高效的目录树遍历和文件过滤
     * Spring应用：扫描类路径、资源文件查找
     */
    private static void demonstrateDirectoryOperations() {
        System.out.println("3. 目录遍历和文件查找\n");

        try {
            // 创建临时目录结构
            Path tempDir = Files.createTempDirectory("nio-demo-");
            Files.createFile(tempDir.resolve("file1.txt"));
            Files.createFile(tempDir.resolve("file2.java"));
            Files.createDirectory(tempDir.resolve("subdir"));
            Files.createFile(tempDir.resolve("subdir/file3.txt"));

            System.out.println("临时目录：" + tempDir);

            // 列出目录内容
            System.out.println("\n✓ 直接子项：");
            try (Stream<Path> entries = Files.list(tempDir)) {
                entries.forEach(path ->
                    System.out.println("  " + (Files.isDirectory(path) ? "[DIR] " : "[FILE] ")
                                     + path.getFileName())
                );
            }

            // 递归遍历所有文件
            System.out.println("\n✓ 递归遍历所有 .txt 文件：");
            try (Stream<Path> paths = Files.walk(tempDir)) {
                paths.filter(Files::isRegularFile)
                     .filter(p -> p.toString().endsWith(".txt"))
                     .forEach(p -> System.out.println("  " + tempDir.relativize(p)));
            }

            // 查找文件
            System.out.println("\n✓ 使用 PathMatcher 查找 .java 文件：");
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");
            try (Stream<Path> paths = Files.walk(tempDir)) {
                paths.filter(matcher::matches)
                     .forEach(p -> System.out.println("  " + p.getFileName()));
            }

            // 清理
            deleteDirectory(tempDir);
            System.out.println("\n✓ 临时目录已清理\n");

        } catch (IOException e) {
            System.err.println("目录操作失败：" + e.getMessage());
        }
    }

    /**
     * 演示 WatchService（文件监听）
     * 作用：监听文件系统变化事件
     * Spring应用：配置文件热加载、开发环境自动重启
     */
    private static void demonstrateWatchService() {
        System.out.println("4. WatchService 文件监听\n");

        try {
            Path tempDir = Files.createTempDirectory("watch-demo-");

            // 创建监听服务
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                // 注册监听事件
                tempDir.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

                System.out.println("✓ 开始监听目录：" + tempDir);
                System.out.println("  监听事件：CREATE, MODIFY, DELETE\n");

                // 在另一个线程中触发文件操作
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        Path file = tempDir.resolve("test.txt");
                        Files.writeString(file, "初始内容");
                        Thread.sleep(100);
                        Files.writeString(file, "修改后的内容");
                        Thread.sleep(100);
                        Files.delete(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                // 等待并处理事件（最多 3 个事件）
                int eventCount = 0;
                while (eventCount < 3) {
                    WatchKey key = watchService.poll(2, java.util.concurrent.TimeUnit.SECONDS);
                    if (key == null) break;

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path filename = (Path) event.context();

                        System.out.println("  检测到事件：" + kind.name() + " - " + filename);
                        eventCount++;
                    }

                    key.reset();
                }

                System.out.println("\n✓ 共捕获 " + eventCount + " 个文件事件");
            }

            // 清理
            deleteDirectory(tempDir);
            System.out.println("✓ 监听已停止\n");

        } catch (Exception e) {
            System.err.println("监听服务失败：" + e.getMessage());
        }
    }

    /**
     * 递归删除目录
     */
    private static void deleteDirectory(Path directory) throws IOException {
        if (Files.exists(directory)) {
            try (Stream<Path> paths = Files.walk(directory)) {
                paths.sorted((p1, p2) -> p2.compareTo(p1)) // 先删除子项
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                         } catch (IOException e) {
                             System.err.println("删除失败：" + path);
                         }
                     });
            }
        }
    }
}

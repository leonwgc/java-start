package io;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Java文件I/O操作学习
 * 学习目标：
 * 1. 掌握文件的读写操作
 * 2. 理解字节流和字符流
 * 3. 学习NIO文件操作
 * 4. 掌握文件和目录操作
 */
public class FileIODemo {
    private static final String FILE_PATH = "test.txt";
    private static final String DIR_PATH = "test_directory";

    public static void main(String[] args) {
        System.out.println("=== 文件I/O操作学习 ===\n");

        try {
            // 示例1：基本文件写入
            demonstrateFileWrite();

            // 示例2：基本文件读取
            demonstrateFileRead();

            // 示例3：使用BufferedReader/Writer
            demonstrateBufferedIO();

            // 示例4：NIO方式操作文件
            demonstrateNIO();

            // 示例5：文件和目录操作
            demonstrateFileOperations();

            // 清理测试文件
            cleanup();

        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 基本文件写入
    public static void demonstrateFileWrite() throws IOException {
        System.out.println("1. 文件写入\n");

        // 使用FileWriter写入文本
        FileWriter writer = new FileWriter(FILE_PATH);
        writer.write("Hello, Java!\n");
        writer.write("这是文件写入测试\n");
        writer.write("学习Java文件操作\n");
        writer.close();

        System.out.println("文件写入完成: " + FILE_PATH);
        System.out.println();
    }

    // 基本文件读取
    public static void demonstrateFileRead() throws IOException {
        System.out.println("2. 文件读取\n");

        // 使用FileReader读取文本
        FileReader reader = new FileReader(FILE_PATH);
        int character;
        System.out.println("文件内容:");
        while ((character = reader.read()) != -1) {
            System.out.print((char) character);
        }
        reader.close();
        System.out.println();
    }

    // 使用BufferedReader/Writer
    public static void demonstrateBufferedIO() throws IOException {
        System.out.println("3. 使用缓冲流（更高效）\n");

        // 写入文件
        String bufferedFile = "buffered_test.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(bufferedFile));
        bw.write("第一行内容");
        bw.newLine();  // 写入换行符
        bw.write("第二行内容");
        bw.newLine();
        bw.write("第三行内容");
        bw.close();

        System.out.println("使用BufferedWriter写入完成");

        // 读取文件（逐行读取）
        BufferedReader br = new BufferedReader(new FileReader(bufferedFile));
        String line;
        int lineNumber = 1;
        System.out.println("\n使用BufferedReader读取:");
        while ((line = br.readLine()) != null) {
            System.out.println("第" + lineNumber + "行: " + line);
            lineNumber++;
        }
        br.close();

        // 清理
        new File(bufferedFile).delete();
        System.out.println();
    }

    // NIO方式操作文件
    public static void demonstrateNIO() throws IOException {
        System.out.println("4. 使用NIO（Java 7+推荐方式）\n");

        Path path = Paths.get("nio_test.txt");

        // 写入文件（一次性写入所有行）
        List<String> lines = Arrays.asList(
            "NIO测试第一行",
            "NIO测试第二行",
            "NIO测试第三行"
        );
        Files.write(path, lines);
        System.out.println("NIO写入完成");

        // 读取文件（一次性读取所有行）
        List<String> readLines = Files.readAllLines(path);
        System.out.println("\nNIO读取内容:");
        for (int i = 0; i < readLines.size(); i++) {
            System.out.println((i + 1) + ". " + readLines.get(i));
        }

        // 追加内容
        Files.write(path, Arrays.asList("追加的新内容"),
                   StandardOpenOption.APPEND);
        System.out.println("\n追加内容后:");
        Files.lines(path).forEach(System.out::println);

        // 清理
        Files.deleteIfExists(path);
        System.out.println();
    }

    // 文件和目录操作
    public static void demonstrateFileOperations() throws IOException {
        System.out.println("5. 文件和目录操作\n");

        // 创建目录
        File directory = new File(DIR_PATH);
        if (directory.mkdir()) {
            System.out.println("创建目录: " + DIR_PATH);
        }

        // 在目录中创建文件
        File file1 = new File(DIR_PATH, "file1.txt");
        File file2 = new File(DIR_PATH, "file2.txt");
        file1.createNewFile();
        file2.createNewFile();
        System.out.println("创建文件: file1.txt, file2.txt");

        // 列出目录中的文件
        System.out.println("\n目录中的文件:");
        String[] files = directory.list();
        if (files != null) {
            for (String fileName : files) {
                System.out.println("  - " + fileName);
            }
        }

        // 文件信息
        System.out.println("\n文件信息:");
        System.out.println("file1.txt 存在: " + file1.exists());
        System.out.println("file1.txt 是文件: " + file1.isFile());
        System.out.println("file1.txt 是目录: " + file1.isDirectory());
        System.out.println("file1.txt 可读: " + file1.canRead());
        System.out.println("file1.txt 可写: " + file1.canWrite());

        // 重命名文件
        File renamedFile = new File(DIR_PATH, "renamed.txt");
        if (file1.renameTo(renamedFile)) {
            System.out.println("\nfile1.txt 重命名为 renamed.txt");
        }

        // 使用NIO获取文件属性
        Path path = renamedFile.toPath();
        System.out.println("\n使用NIO获取文件大小: " + Files.size(path) + " 字节");
        System.out.println();
    }

    // 清理测试文件
    public static void cleanup() {
        System.out.println("6. 清理测试文件");

        try {
            // 删除测试文件
            new File(FILE_PATH).delete();

            // 删除目录中的文件
            File directory = new File(DIR_PATH);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                directory.delete();
            }

            System.out.println("清理完成！");
        } catch (Exception e) {
            System.err.println("清理失败: " + e.getMessage());
        }
    }
}

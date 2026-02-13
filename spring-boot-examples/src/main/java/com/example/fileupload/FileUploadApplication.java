package com.example.fileupload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file. Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Spring Boot 文件上传下载示例
 * 学习目标：
 * 1. 掌握MultipartFile处理文件上传
 * 2. 学会文件的存储和管理
 * 3. 实现文件下载功能
 * 4. 掌握文件类型验证和大小限制
 * 5. 了解文件上传的安全性考虑
 *
 * 核心知识点：
 * - MultipartFile：Spring提供的文件上传接口
 * - Path/Files：Java NIO文件操作API
 * - Resource：Spring资源抽象
 * - Content-Disposition：HTTP头控制文件下载
 *
 * 应用场景：
 * - 用户头像上传
 * - 文档管理系统
 * - 图片管理
 * - 文件分享
 */
@SpringBootApplication
@Slf4j
public class FileUploadApplication {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot 文件上传下载示例 ===\n");
        System.out.println("学习内容：");
        System.out.println("1. MultipartFile文件上传");
        System.out.println("2. 多文件上传处理");
        System.out.println("3. 文件类型和大小验证");
        System.out.println("4. 文件存储管理");
        System.out.println("5. 文件下载实现\n");

        SpringApplication.run(FileUploadApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 测试地址：http://localhost:8080/api/files");
        System.out.println("\n📝 测试示例：");
        System.out.println("# 上传文件");
        System.out.println("curl -X POST -F \"file=@test.txt\" http://localhost:8080/api/files/upload");
        System.out.println("\n# 查看所有文件");
        System.out.println("curl http://localhost:8080/api/files");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }
}

/**
 * 文件存储服务
 */
@Slf4j
@Service
class FileStorageService {

    private final Path fileStorageLocation;

    // 允许的文件类型
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "txt", "pdf", "doc", "docx", "xls", "xlsx",
        "jpg", "jpeg", "png", "gif", "svg"
    );

    // 最大文件大小：10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public FileStorageService() {
        // 设置文件存储目录
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("📁 文件存储目录创建成功: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("❌ 无法创建文件存储目录", ex);
            throw new RuntimeException("无法创建文件存储目录", ex);
        }
    }

    /**
     * 存储文件
     */
    public FileInfo storeFile(MultipartFile file) {
        // 1. 验证文件
        validateFile(file);

        // 2. 获取原始文件名
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // 3. 生成唯一文件名（防止重名）
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;

        try {
            // 4. 检查文件名是否包含非法字符
            if (originalFilename.contains("..")) {
                throw new RuntimeException("文件名包含非法字符: " + originalFilename);
            }

            // 5. 保存文件
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("✅ 文件保存成功: {} -> {}", originalFilename, uniqueFilename);

            // 6. 返回文件信息
            return new FileInfo(
                uniqueFilename,
                originalFilename,
                file.getContentType(),
                file.getSize(),
                LocalDateTime.now(),
                generateDownloadUrl(uniqueFilename)
            );

        } catch (IOException ex) {
            log.error("❌ 文件保存失败: {}", originalFilename, ex);
            throw new RuntimeException("文件保存失败: " + originalFilename, ex);
        }
    }

    /**
     * 加载文件为Resource
     */
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("文件未找到: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("文件未找到: " + filename, ex);
        }
    }

    /**
     * 获取所有文件列表
     */
    public List<FileInfo> getAllFiles() {
        try (Stream<Path> paths = Files.walk(this.fileStorageLocation, 1)) {
            return paths
                .filter(Files::isRegularFile)
                .map(path -> {
                    String filename = path.getFileName().toString();
                    try {
                        long size = Files.size(path);
                        return new FileInfo(
                            filename,
                            filename,
                            getContentType(filename),
                            size,
                            LocalDateTime.now(),
                            generateDownloadUrl(filename)
                        );
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("❌ 获取文件列表失败", ex);
            return Collections.emptyList();
        }
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            log.error("❌ 删除文件失败: {}", filename, ex);
            return false;
        }
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("文件大小超过限制（最大10MB）");
        }

        // 检查文件类型
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new RuntimeException("不支持的文件类型: " + extension);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 获取Content-Type
     */
    private String getContentType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        return switch (extension) {
            case "txt" -> "text/plain";
            case "pdf" -> "application/pdf";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }

    /**
     * 生成下载URL
     */
    private String generateDownloadUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(filename)
                .toUriString();
    }
}

/**
 * 文件信息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class FileInfo {
    private String filename;
    private String originalFilename;
    private String contentType;
    private Long size;
    private LocalDateTime uploadTime;
    private String downloadUrl;
}

/**
 * 统一响应对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class ApiResponse {
    private Integer code;
    private String message;
    private Object data;
}

/**
 * 文件管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/files")
class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * 1. 单文件上传
     * POST /api/files/upload
     * curl -X POST -F "file=@test.txt" http://localhost:8080/api/files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileInfo fileInfo = fileStorageService.storeFile(file);

            return ResponseEntity.ok(new ApiResponse(
                200,
                "文件上传成功",
                fileInfo
            ));
        } catch (Exception ex) {
            log.error("文件上传失败", ex);
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, "文件上传失败: " + ex.getMessage(), null));
        }
    }

    /**
     * 2. 多文件上传
     * POST /api/files/upload-multiple
     * curl -X POST -F "files=@file1.txt" -F "files=@file2.txt" http://localhost:8080/api/files/upload-multiple
     */
    @PostMapping("/upload-multiple")
    public ResponseEntity<ApiResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            List<FileInfo> fileInfos = new ArrayList<>();

            for (MultipartFile file : files) {
                FileInfo fileInfo = fileStorageService.storeFile(file);
                fileInfos.add(fileInfo);
            }

            return ResponseEntity.ok(new ApiResponse(
                200,
                "文件上传成功，共" + fileInfos.size() + "个文件",
                fileInfos
            ));
        } catch (Exception ex) {
            log.error("文件上传失败", ex);
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(400, "文件上传失败: " + ex.getMessage(), null));
        }
    }

    /**
     * 3. 下载文件
     * GET /api/files/download/{filename}
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(filename);

            // 设置Content-Disposition头，让浏览器下载文件
            String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);

        } catch (Exception ex) {
            log.error("文件下载失败: {}", filename, ex);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 4. 获取所有文件列表
     * GET /api/files
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllFiles() {
        List<FileInfo> files = fileStorageService.getAllFiles();

        return ResponseEntity.ok(new ApiResponse(
            200,
            "获取文件列表成功",
            Map.of(
                "totalFiles", files.size(),
                "files", files
            )
        ));
    }

    /**
     * 5. 删除文件
     * DELETE /api/files/{filename}
     */
    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable String filename) {
        boolean deleted = fileStorageService.deleteFile(filename);

        if (deleted) {
            return ResponseEntity.ok(new ApiResponse(
                200,
                "文件删除成功",
                filename
            ));
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, "文件未找到或删除失败", filename));
        }
    }

    /**
     * 6. 获取使用指南
     * GET /api/files/guide
     */
    @GetMapping("/guide")
    public ResponseEntity<Map<String, Object>> getGuide() {
        Map<String, Object> guide = new HashMap<>();

        guide.put("文件上传限制", Map.of(
            "最大文件大小", "10MB",
            "支持的文件类型", "txt, pdf, doc, docx, xls, xlsx, jpg, jpeg, png, gif, svg"
        ));

        guide.put("API接口", Map.of(
            "单文件上传", "POST /api/files/upload",
            "多文件上传", "POST /api/files/upload-multiple",
            "文件下载", "GET /api/files/download/{filename}",
            "文件列表", "GET /api/files",
            "删除文件", "DELETE /api/files/{filename}"
        ));

        guide.put("curl测试命令", Map.of(
            "上传文件", "curl -X POST -F \"file=@yourfile.txt\" http://localhost:8080/api/files/upload",
            "上传多个文件", "curl -X POST -F \"files=@file1.txt\" -F \"files=@file2.txt\" http://localhost:8080/api/files/upload-multiple",
            "下载文件", "curl -O http://localhost:8080/api/files/download/filename.txt",
            "查看所有文件", "curl http://localhost:8080/api/files",
            "删除文件", "curl -X DELETE http://localhost:8080/api/files/filename.txt"
        ));

        guide.put("注意事项", List.of(
            "文件名会自动生成UUID以防止重名",
            "上传的文件存储在项目的uploads目录",
            "下载文件时会设置Content-Disposition为attachment强制下载",
            "文件类型和大小都会进行验证"
        ));

        return ResponseEntity.ok(guide);
    }
}

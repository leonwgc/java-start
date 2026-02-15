package com.example.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Boot 事务管理学习示例
 * 学习目标：
 * 1. 理解Spring事务管理机制
 * 2. 掌握@Transactional注解的使用
 * 3. 学习事务传播行为和隔离级别
 * 4. 了解事务回滚规则和最佳实践
 *
 * 运行方式：
 * ./run-app.sh transaction
 *
 * 测试接口：
 * # 查看所有账户
 * curl http://localhost:8080/api/accounts
 *
 * # 成功转账（事务提交）
 * curl -X POST "http://localhost:8080/api/transfer?from=1&to=2&amount=100"
 *
 * # 失败转账（事务回滚）
 * curl -X POST "http://localhost:8080/api/transfer?from=1&to=2&amount=10000"
 *
 * # 测试传播行为
 * curl -X POST http://localhost:8080/api/test-propagation
 *
 * # 测试只读事务
 * curl http://localhost:8080/api/accounts/readonly/1
 */
@Slf4j
@SpringBootApplication
public class TransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
        log.info("===========================================");
        log.info("事务管理示例启动成功！");
        log.info("测试地址：http://localhost:8080");
        log.info("===========================================");
    }

    /**
     * 初始化测试数据
     */
    @Bean
    CommandLineRunner initData(AccountRepository accountRepository,
                               TransactionLogRepository logRepository) {
        return args -> {
            // 清空旧数据
            accountRepository.deleteAll();
            logRepository.deleteAll();

            // 创建测试账户
            Account account1 = new Account();
            account1.setAccountNumber("ACC001");
            account1.setAccountName("张三");
            account1.setBalance(new BigDecimal("1000.00"));
            accountRepository.save(account1);

            Account account2 = new Account();
            account2.setAccountNumber("ACC002");
            account2.setAccountName("李四");
            account2.setBalance(new BigDecimal("500.00"));
            accountRepository.save(account2);

            Account account3 = new Account();
            account3.setAccountNumber("ACC003");
            account3.setAccountName("王五");
            account3.setBalance(new BigDecimal("800.00"));
            accountRepository.save(account3);

            log.info("初始化测试数据完成：3个账户");
        };
    }
}

/**
 * 账户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;  // 账号

    @Column(nullable = false)
    private String accountName;    // 账户名

    @Column(nullable = false)
    private BigDecimal balance;    // 余额

    @Version  // 乐观锁版本号
    private Long version;

    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

/**
 * 交易日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_logs")
class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromAccountId;    // 转出账户
    private Long toAccountId;      // 转入账户
    private BigDecimal amount;     // 金额
    private String status;         // 状态：SUCCESS/FAILED
    private String errorMessage;   // 错误信息
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

/**
 * 账户Repository
 */
@Repository
interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
}

/**
 * 交易日志Repository
 */
@Repository
interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}

/**
 * 账户服务 - 演示事务管理
 */
@Slf4j
@Service
class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository logRepository;

    public AccountService(AccountRepository accountRepository,
                         TransactionLogRepository logRepository) {
        this.accountRepository = accountRepository;
        this.logRepository = logRepository;
    }

    /**
     * 1. 基本事务：转账操作
     * 默认在RuntimeException时回滚
     */
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        log.info("开始转账：从账户{}到账户{}，金额{}", fromId, toId, amount);

        // 查询账户
        Account fromAccount = accountRepository.findById(fromId)
            .orElseThrow(() -> new RuntimeException("转出账户不存在"));
        Account toAccount = accountRepository.findById(toId)
            .orElseThrow(() -> new RuntimeException("转入账户不存在"));

        // 检查余额
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 扣款
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // 模拟处理时间
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 入账
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // 记录日志
        TransactionLog txLog = new TransactionLog();
        txLog.setFromAccountId(fromId);
        txLog.setToAccountId(toId);
        txLog.setAmount(amount);
        txLog.setStatus("SUCCESS");
        logRepository.save(txLog);

        log.info("转账成功");
    }

    /**
     * 2. 事务传播行为 - REQUIRED（默认）
     * 如果当前存在事务，则加入该事务；如果没有事务，则创建新事务
     */
    @Transactional
    public void transferWithLog(Long fromId, Long toId, BigDecimal amount) {
        try {
            transfer(fromId, toId, amount);  // 加入当前事务
        } catch (Exception e) {
            log.error("转账失败：{}", e.getMessage());
            // 记录失败日志（在同一事务中）
            TransactionLog txLog = new TransactionLog();
            txLog.setFromAccountId(fromId);
            txLog.setToAccountId(toId);
            txLog.setAmount(amount);
            txLog.setStatus("FAILED");
            txLog.setErrorMessage(e.getMessage());
            logRepository.save(txLog);
            throw e;  // 重新抛出异常，触发回滚
        }
    }

    /**
     * 3. 事务传播行为 - REQUIRES_NEW
     * 无论当前是否存在事务，都创建新事务
     * 新事务独立提交或回滚，不影响外部事务
     */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void logTransaction(Long fromId, Long toId, BigDecimal amount, String status, String errorMsg) {
        TransactionLog txLog = new TransactionLog();
        txLog.setFromAccountId(fromId);
        txLog.setToAccountId(toId);
        txLog.setAmount(amount);
        txLog.setStatus(status);
        txLog.setErrorMessage(errorMsg);
        logRepository.save(txLog);
        log.info("记录交易日志（独立事务）：{}", status);
    }

    /**
     * 4. 只读事务
     * 优化查询性能，不允许修改操作
     */
    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("账户不存在"));
    }

    /**
     * 5. 事务超时设置
     * timeout单位是秒
     */
    @Transactional(timeout = 5)
    public void transferWithTimeout(Long fromId, Long toId, BigDecimal amount) {
        transfer(fromId, toId, amount);
    }

    /**
     * 6. 指定回滚异常
     * 默认只在RuntimeException和Error时回滚
     * 可以通过rollbackFor指定其他异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferWithRollbackFor(Long fromId, Long toId, BigDecimal amount) throws Exception {
        transfer(fromId, toId, amount);
        // 即使是检查异常也会回滚
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            throw new Exception("单笔转账金额不能超过1000");
        }
    }

    /**
     * 7. 不回滚指定异常
     */
    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public void transferNoRollback(Long fromId, Long toId, BigDecimal amount) {
        transfer(fromId, toId, amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("金额必须大于0");
        }
    }

    /**
     * 获取所有账户
     */
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * 获取所有交易日志
     */
    @Transactional(readOnly = true)
    public List<TransactionLog> getAllLogs() {
        return logRepository.findAll();
    }
}

/**
 * REST控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
class TransactionController {

    private final AccountService accountService;

    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 获取所有账户
     */
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    /**
     * 获取指定账户（只读事务）
     */
    @GetMapping("/accounts/readonly/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    /**
     * 获取所有交易日志
     */
    @GetMapping("/logs")
    public List<TransactionLog> getAllLogs() {
        return accountService.getAllLogs();
    }

    /**
     * 转账（基本事务）
     */
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long from,
                          @RequestParam Long to,
                          @RequestParam BigDecimal amount) {
        try {
            accountService.transfer(from, to, amount);
            return "转账成功：" + amount + "元";
        } catch (Exception e) {
            log.error("转账失败", e);
            return "转账失败：" + e.getMessage();
        }
    }

    /**
     * 测试事务传播行为
     */
    @PostMapping("/test-propagation")
    public String testPropagation() {
        try {
            // 外部事务
            accountService.transferWithLog(1L, 2L, new BigDecimal("50"));
            return "测试传播行为成功";
        } catch (Exception e) {
            return "测试传播行为失败：" + e.getMessage();
        }
    }

    /**
     * 测试独立事务（REQUIRES_NEW）
     */
    @PostMapping("/test-requires-new")
    public String testRequiresNew() {
        try {
            // 即使外部事务失败，日志也会被记录
            accountService.logTransaction(1L, 2L, new BigDecimal("100"), "TEST", "测试独立事务");
            // 故意抛出异常
            throw new RuntimeException("外部事务失败");
        } catch (Exception e) {
            return "外部事务回滚，但日志已记录（独立事务）";
        }
    }

    /**
     * 帮助文档
     */
    @GetMapping("/help")
    public String help() {
        return """
            === Spring Boot 事务管理示例 ===

            1. 查看所有账户
               GET http://localhost:8080/api/accounts

            2. 查看交易日志
               GET http://localhost:8080/api/logs

            3. 转账（成功）
               POST http://localhost:8080/api/transfer?from=1&to=2&amount=100

            4. 转账（失败-余额不足）
               POST http://localhost:8080/api/transfer?from=1&to=2&amount=10000

            5. 测试事务传播
               POST http://localhost:8080/api/test-propagation

            6. 测试独立事务
               POST http://localhost:8080/api/test-requires-new

            核心知识点：
            - @Transactional：声明式事务
            - propagation：事务传播行为
            - isolation：事务隔离级别
            - timeout：事务超时
            - readOnly：只读事务优化
            - rollbackFor：指定回滚异常
            - @Version：乐观锁防止并发问题

            事务传播行为：
            - REQUIRED（默认）：有事务加入，无事务创建
            - REQUIRES_NEW：总是创建新事务
            - SUPPORTS：有事务加入，无事务非事务运行
            - NOT_SUPPORTED：总是非事务运行
            - MANDATORY：必须有事务，否则抛异常
            - NEVER：禁止事务，有事务抛异常
            - NESTED：嵌套事务（使用保存点）
            """;
    }
}

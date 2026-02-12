package advanced;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Java 序列化和反序列化演示
 * 学习目标：
 * 1. 理解 Java 序列化机制和 Serializable 接口
 * 2. 掌握 transient 关键字和自定义序列化
 * 3. 理解序列化版本控制（serialVersionUID）
 *
 * Spring应用场景：
 * - Session 会话持久化（Redis、数据库）
 * - 分布式缓存数据存储
 * - RPC 远程方法调用参数传递
 * - 深拷贝对象复制
 */
public class SerializationDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Java 序列化和反序列化学习 ===\n");

        demonstrateBasicSerialization();
        demonstrateTransientKeyword();
        demonstrateSerialVersionUID();
        demonstrateCustomSerialization();
        demonstrateDeepCopy();
    }

    /**
     * 演示基本的序列化和反序列化
     * 作用：将对象转换为字节流进行存储或传输
     * Spring应用：Session 会话数据持久化
     */
    private static void demonstrateBasicSerialization() throws Exception {
        System.out.println("1. 基本序列化与反序列化\n");

        // 创建用户对象
        User user = new User("zhangsan", "password123", "zhang@example.com");
        System.out.println("原始对象：" + user);

        // 序列化到文件
        Path tempFile = Files.createTempFile("user-", ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(tempFile))) {
            oos.writeObject(user);
            System.out.println("✓ 已序列化到文件：" + tempFile);
            System.out.println("  文件大小：" + Files.size(tempFile) + " 字节");
        }

        // 从文件反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(tempFile))) {
            User deserializedUser = (User) ois.readObject();
            System.out.println("✓ 反序列化对象：" + deserializedUser);
            System.out.println("  对象相等性：" + user.equals(deserializedUser));
            System.out.println("  引用相等性：" + (user == deserializedUser)); // false
        }

        Files.deleteIfExists(tempFile);
        System.out.println();
    }

    /**
     * 演示 transient 关键字
     * 作用：标记不需要序列化的字段（如密码、缓存数据）
     * Spring应用：敏感信息保护、缓存字段排除
     */
    private static void demonstrateTransientKeyword() throws Exception {
        System.out.println("2. transient 关键字（字段排除）\n");

        User user = new User("lisi", "secret@456", "li@example.com");
        System.out.println("序列化前：" + user);

        // 序列化到内存
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(user);
        }

        byte[] serializedData = baos.toByteArray();
        System.out.println("✓ 序列化后数据大小：" + serializedData.length + " 字节");

        // 从内存反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(serializedData))) {
            User deserializedUser = (User) ois.readObject();
            System.out.println("✓ 反序列化后：" + deserializedUser);
        }

        System.out.println("\n说明：");
        System.out.println("- password 字段被标记为 transient");
        System.out.println("- 反序列化后 password = null（未被保存）");
        System.out.println("- Spring Session 使用类似机制保护敏感数据\n");
    }

    /**
     * 演示 serialVersionUID 版本控制
     * 作用：确保序列化和反序列化使用兼容的类版本
     * Spring应用：系统升级时的兼容性保障
     */
    private static void demonstrateSerialVersionUID() throws Exception {
        System.out.println("3. serialVersionUID 版本控制\n");

        Product product = new Product("笔记本电脑", 5999.99, "电子产品");
        System.out.println("产品对象：" + product);

        // 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(product);
        }
        System.out.println("✓ 产品已序列化");

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {
            Product deserializedProduct = (Product) ois.readObject();
            System.out.println("✓ 产品已反序列化：" + deserializedProduct);
        }

        System.out.println("\nserialVersionUID 作用：");
        System.out.println("- 标识类的版本号（1L, 2L, 3L...）");
        System.out.println("- 反序列化时检查版本是否匹配");
        System.out.println("- 如果不匹配，抛出 InvalidClassException");
        System.out.println("- 建议显式声明，避免类修改后无法反序列化");
        System.out.println("\n最佳实践：");
        System.out.println("private static final long serialVersionUID = 1L;");
        System.out.println("// 修改类结构时，根据兼容性决定是否修改版本号\n");
    }

    /**
     * 演示自定义序列化
     * 作用：完全控制序列化过程，实现特殊逻辑
     * Spring应用：加密敏感字段、压缩大对象
     */
    private static void demonstrateCustomSerialization() throws Exception {
        System.out.println("4. 自定义序列化（writeObject/readObject）\n");

        Account account = new Account("account123", 10000.0, "PIN-9876");
        System.out.println("账户原始数据：" + account);

        // 序列化（会调用自定义的 writeObject）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(account);
        }
        System.out.println("✓ 账户已序列化（PIN 已加密）");

        // 反序列化（会调用自定义的 readObject）
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {
            Account deserializedAccount = (Account) ois.readObject();
            System.out.println("✓ 账户已反序列化：" + deserializedAccount);
        }

        System.out.println("\n自定义序列化方法：");
        System.out.println("private void writeObject(ObjectOutputStream oos) {");
        System.out.println("    // 自定义写入逻辑（如加密）");
        System.out.println("}");
        System.out.println("private void readObject(ObjectInputStream ois) {");
        System.out.println("    // 自定义读取逻辑（如解密）");
        System.out.println("}\n");
    }

    /**
     * 演示深拷贝（通过序列化）
     * 作用：完全复制对象，不共享引用
     * Spring应用：原型模式 Bean 复制
     */
    private static void demonstrateDeepCopy() throws Exception {
        System.out.println("5. 深拷贝（序列化方式）\n");

        // 创建原始对象
        List<String> tags = new ArrayList<>();
        tags.add("Java");
        tags.add("Spring");
        Product original = new Product("Java书籍", 89.0, "图书");

        System.out.println("原始对象：" + original);

        // 通过序列化实现深拷贝
        Product cloned = deepCopy(original);
        System.out.println("克隆对象：" + cloned);

        // 验证深拷贝（修改原始对象不影响克隆对象）
        original.setPrice(99.0);
        System.out.println("\n修改原始对象价格后：");
        System.out.println("  原始对象：" + original);
        System.out.println("  克隆对象：" + cloned);
        System.out.println("  克隆对象不受影响：" + (cloned.getPrice() == 89.0));

        System.out.println("\n深拷贝 vs 浅拷贝：");
        System.out.println("- 浅拷贝：只复制对象本身，引用类型字段共享");
        System.out.println("- 深拷贝：完全复制对象及所有引用对象");
        System.out.println("- Spring @Scope(\"prototype\") 使用类似机制");
    }

    /**
     * 通过序列化实现深拷贝
     */
    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T deepCopy(T object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {
            return (T) ois.readObject();
        }
    }

    // ==================== 内部类定义 ====================

    /**
     * 用户类（演示基本序列化和 transient）
     */
    static class User implements Serializable {
        private static final long serialVersionUID = 1L;

        private String username;
        private transient String password; // 不序列化密码
        private String email;

        public User(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        @Override
        public String toString() {
            return String.format("User{username='%s', password='%s', email='%s'}",
                username, password, email);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof User)) return false;
            User other = (User) obj;
            return username.equals(other.username) && email.equals(other.email);
        }
    }

    /**
     * 产品类（演示 serialVersionUID）
     */
    static class Product implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private double price;
        private String category;

        public Product(String name, double price, String category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return String.format("Product{name='%s', price=%.2f, category='%s'}",
                name, price, category);
        }
    }

    /**
     * 账户类（演示自定义序列化）
     */
    static class Account implements Serializable {
        private static final long serialVersionUID = 1L;

        private String accountNumber;
        private double balance;
        private transient String pin; // 标记为 transient，但在自定义方法中加密保存

        public Account(String accountNumber, double balance, String pin) {
            this.accountNumber = accountNumber;
            this.balance = balance;
            this.pin = pin;
        }

        /**
         * 自定义序列化方法
         */
        private void writeObject(ObjectOutputStream oos) throws IOException {
            // 先写入默认字段
            oos.defaultWriteObject();

            // 加密 PIN 后写入（简单示例：反转字符串）
            String encryptedPin = new StringBuilder(pin).reverse().toString();
            oos.writeObject(encryptedPin);
        }

        /**
         * 自定义反序列化方法
         */
        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            // 先读取默认字段
            ois.defaultReadObject();

            // 读取并解密 PIN
            String encryptedPin = (String) ois.readObject();
            this.pin = new StringBuilder(encryptedPin).reverse().toString();
        }

        @Override
        public String toString() {
            return String.format("Account{accountNumber='%s', balance=%.2f, pin='%s'}",
                accountNumber, balance, pin);
        }
    }
}

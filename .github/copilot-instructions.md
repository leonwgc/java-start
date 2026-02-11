# AI Coding Guidelines - java-start

## 1. Code Style

**Chinese Comments with Learning Objectives** - Every class must start with:
```java
/**
 * [主题描述]
 * 学习目标：
 * 1. [目标1]
 * 2. [目标2]
 * 3. [目标3]
 */
```
See [Person.java](src/oop/Person.java), [StreamDemo.java](src/advanced/StreamDemo.java)

**Self-Contained Main Methods** - Each demo class has `main()` calling `demonstrate<Feature>()` methods:
```java
public static void main(String[] args) {
    System.out.println("=== 主题标题 ===\n");
    demonstrateFeature1();
    demonstrateFeature2();
}
```

**Console Output for Learning** - Use descriptive section headers and explain concepts:
```java
System.out.println("1. 概念说明\n");
System.out.println("作用：[功能描述]");
System.out.println("Spring应用：[如何用于Spring]\n");
```

## 2. Architecture

**Learning Progression Packages** (basics → oop → collections → io → advanced → spring):
- `basics/` - HelloWorld, DataTypes, ControlFlow, Methods, ExceptionHandling
- `oop/` - Person, inheritance, interfaces, abstract classes
- `collections/` - Lists, Sets, Maps operations
- `advanced/` - Annotations, Reflection, Streams, Lambda, Threads, Design Patterns
- `spring/` - Spring Framework preparation (empty, future use)

**Non-Standard Maven Layout** - Source is `src/` not `src/main/java`, configured in [pom.xml](pom.xml#L60)

## 3. Build and Test

```bash
mvn clean compile    # 编译项目
mvn exec:java -Dexec.mainClass="basics.HelloWorld"  # 运行示例
```

**Testing** - JUnit 5 configured but no tests written. When adding tests:
- Follow same Chinese comment style
- Name test classes `<ClassName>Test.java`
- Place in corresponding package under test/

## 4. Project Conventions

**Spring Framework Preparation Focus**:
- [AnnotationDemo.java](src/advanced/AnnotationDemo.java) - Custom `@Service`, `@Component` style annotations
- [DesignPatternsDemo.java](src/advanced/DesignPatternsDemo.java) - Patterns used by Spring (Singleton, Factory, Proxy)
- [ReflectionDemo.java](src/advanced/ReflectionDemo.java) - Runtime inspection used by Spring

**Educational Design**:
- Each class is standalone and runnable independently
- No cross-file dependencies except basic imports
- Comprehensive console output explains concepts as code runs
- Examples progress from simple to complex within each file

## 5. Integration Points

**Dependencies Available** (but minimally used currently):
- Lombok (`@Data`, `@Builder`) - available for reducing boilerplate
- SLF4J + Logback - can replace `System.out` for real logging examples
- JUnit 5 - ready for test-driven learning modules

**When Adding Code**:
1. Match Chinese comment style with "学习目标："
2. Add self-contained main() for immediate testing
3. Include console output explaining what's being demonstrated
4. Reference Spring concepts where applicable (this is the ultimate goal)
5. Keep examples progressive: simple → intermediate → advanced

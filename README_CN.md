
# PineTool

[English Version](README.md)

PineTool 是一个工具，旨在简化使用 [Pine](https://github.com/canyie/pine) 库进行动态方法钩子和替换的过程，适用于 Android 应用程序。

## 入门指南

### 项目介绍

PineTool 提供了一个简洁的接口来钩住方法，允许开发者在方法调用之前、之后或代替原方法执行自定义行为。无论是用于调试、修改应用行为还是测试，PineTool 都提供了一个灵活而强大的解决方案。

### 导入

在 `build.gradle` 文件中添加以下依赖项：

```groovy
implementation("top.canyie.pine:core:0.2.9")
```

### 初始化

在使用 PineTool 之前，您需要按照以下配置 Pine 设置：

```java
PineConfig.debug = true; // 启用详细日志用于调试
PineConfig.debuggable = true; // 设置应用是否可调试，建议与应用的构建配置保持一致
```

### 使用例子

以下是一些示例，展示了如何使用 PineTool 钩住方法：

```java
// 如果使用 ReplacementHook，不能同时使用 after 和 before 钩子

// 在 method_1 执行之后钩住
Pina.after(
    Main.class.getDeclaredMethod("method_1"),
    callFrame -> {
        Log.d(TAG, "I am method_1 after");
    });

// 在 method_1 执行之前钩住
Pina.before(
    Main.class.getDeclaredMethod("method_1"),
    callFrame -> {
        Log.d(TAG, "I am method_1 before");
    });

// 完全替换 method_2 的实现
Pina.replace(
    Main.class.getDeclaredMethod("method_2"),
    callFrame -> {
        Log.d(TAG, "I am method_2 replacement");
        return null; // 返回 null 作为方法的结果
    });
```

## 版权

该项目在 GNU General Public License v3.0 许可证下发布。

## 致谢

特别感谢 [Pine](https://github.com/canyie/pine) 库提供的底层钩子机制。

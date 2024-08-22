
# PineTool

[English Version](README.md)

PineTool 是一个工具，旨在简化使用 [Pine](https://github.com/canyie/pine) 库进行动态方法Hook和替换的过程，仅适用于 Android。

## 入门指南

### 项目介绍

PineTool 提供了简洁Lambda来Hook方法，允许开发者在方法调用after、before或代替原方法。用于调试、修改应用行为还是测试，PineTool 都提供了一个简便的解决方案和独特的风格。
- 我们使用Pina这个类来进行Hook，目的也是为了和Pine使用类似，减少学习成本
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
以下是一些示例，展示了如何使用 PineTool Hook方法：

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

特别感谢 [Pine](https://github.com/canyie/pine) 库提供的底层Hook Framework。

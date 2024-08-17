
# PineTool

[中文版本](README_CN.md)

PineTool is a utility designed to simplify the use of the [Pine](https://github.com/canyie/pine) library for dynamic method hooking and replacement in Android applications.

## Getting Started

### Project Introduction

PineTool provides a straightforward interface to hook into methods, allowing developers to add custom behavior before, after, or instead of method calls. Whether for debugging, modifying app behavior, or testing, PineTool offers a flexible and powerful solution.

### Installation

Add the following dependency to your `build.gradle` file:

```groovy
implementation("top.canyie.pine:core:0.2.9")
```

### Initialization

Before using PineTool, you need to configure Pine settings as shown below:

```java
PineConfig.debug = true; // Enable detailed logging for debugging
PineConfig.debuggable = true; // Set whether the app is debuggable, should match your app's build configuration
```

### Usage Example

Below are some examples demonstrating how to use PineTool to hook into methods:

```java
// If you use ReplacementHook, you can't use after and before hooks simultaneously

// Hook after the execution of method_1
Pina.after(
    Main.class.getDeclaredMethod("method_1"),
    callFrame -> {
        Log.d(TAG, "I am method_1 after");
    });

// Hook before the execution of method_1
Pina.before(
    Main.class.getDeclaredMethod("method_1"),
    callFrame -> {
        Log.d(TAG, "I am method_1 before");
    });

// Replace method_2 entirely
Pina.replace(
    Main.class.getDeclaredMethod("method_2"),
    callFrame -> {
        Log.d(TAG, "I am method_2 replacement");
        return null; // Return null as the method's result
    });
```

## License

This project is licensed under the GNU General Public License v3.0.

## Acknowledgments

Special thanks to the [Pine](https://github.com/canyie/pine) library for providing the underlying hooking mechanism.

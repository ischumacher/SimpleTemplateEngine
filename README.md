# SimpleTemplateEngine

A JSON-oriented Simple Template Engine written in Kotlin.

## Overview

SimpleTemplateEngine is a lightweight, standalone template engine designed to be downloaded and included directly in your Kotlin source code. No build configuration, external dependencies, or complex setup required—just copy `SimpleTemplateEngine.kt` into your project and start using it.

## Features

- **Simple Variable Interpolation**: Use `{{variable}}` syntax to insert values
- **Nested Property Access**: Access nested properties with dot notation (e.g., `{{user.name}}`)
- **Conditional Rendering**: Use `{{#if condition}}...{{/if}}` blocks
- **Loop Rendering**: Iterate over collections with `{{#each items}}...{{/each}}`
- **JSON-Oriented**: Works seamlessly with Map-based data structures
- **Zero Dependencies**: Only requires Kotlin standard library
- **Single File**: Just copy and paste into your project

## Installation

Simply download `SimpleTemplateEngine.kt` and include it in your project. No additional setup needed.

```bash
# Download the file
curl -O https://raw.githubusercontent.com/ischumacher/SimpleTemplateEngine/main/SimpleTemplateEngine.kt

# Or just copy-paste the content into your project
```

## Usage

### Basic Example

```kotlin
val template = "Hello, {{name}}! You have {{count}} messages."
val data = mapOf("name" to "Alice", "count" to 5)
val result = SimpleTemplateEngine.render(template, data)
// Result: "Hello, Alice! You have 5 messages."
```

### Nested Properties

```kotlin
val template = "User: {{user.name}}, Email: {{user.email}}"
val data = mapOf(
    "user" to mapOf(
        "name" to "Bob",
        "email" to "bob@example.com"
    )
)
val result = SimpleTemplateEngine.render(template, data)
// Result: "User: Bob, Email: bob@example.com"
```

### Conditional Rendering

```kotlin
val template = """
    {{#if loggedIn}}
    Welcome back, {{username}}!
    {{/if}}
""".trimIndent()

val data = mapOf("loggedIn" to true, "username" to "Charlie")
val result = SimpleTemplateEngine.render(template, data)
// Result: "Welcome back, Charlie!"
```

### Loop Rendering

```kotlin
val template = """
    Shopping List:
    {{#each items}}
    - {{name}}: ${'$'}{{price}}
    {{/each}}
""".trimIndent()

val data = mapOf(
    "items" to listOf(
        mapOf("name" to "Apple", "price" to 1.50),
        mapOf("name" to "Banana", "price" to 0.75)
    )
)
val result = SimpleTemplateEngine.render(template, data)
```

### Complex Nested Example

```kotlin
val template = """
    {{#if user}}
    Profile: {{user.name}}
    {{#if user.messages}}
    Messages:
    {{#each user.messages}}
      * {{title}}: {{body}}
    {{/each}}
    {{/if}}
    {{/if}}
""".trimIndent()

val data = mapOf(
    "user" to mapOf(
        "name" to "Diana",
        "messages" to listOf(
            mapOf("title" to "Hello", "body" to "How are you?"),
            mapOf("title" to "Reminder", "body" to "Meeting at 3pm")
        )
    )
)
val result = SimpleTemplateEngine.render(template, data)
```

## Running the Example

An example file is provided to demonstrate various usage patterns:

```bash
# Compile and run
kotlinc SimpleTemplateEngine.kt Example.kt -include-runtime -d example.jar
java -jar example.jar
```

## Syntax Reference

### Variable Interpolation
- `{{variable}}` - Inserts the value of `variable`
- `{{object.property}}` - Accesses nested properties

### Conditionals
```
{{#if condition}}
  Content shown when condition is truthy
{{/if}}
```

Truthy values: non-null, non-empty strings, non-zero numbers, true booleans, non-empty collections

### Loops
```
{{#each collection}}
  {{property}} - Access properties of each item
{{/each}}
```

## License

This is free and unencumbered software released into the public domain. See the repository for more details.

## Contributing

Contributions are welcome! Since this is a single-file library, please ensure your changes maintain backward compatibility and don't introduce external dependencies.

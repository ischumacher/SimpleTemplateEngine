/**
 * Example usage of SimpleTemplateEngine
 * 
 * This file demonstrates how to use the SimpleTemplateEngine in various scenarios.
 * To run this example, simply compile and execute it with Kotlin:
 * 
 * kotlinc SimpleTemplateEngine.kt Example.kt -include-runtime -d example.jar && java -jar example.jar
 * or
 * kotlinc -script Example.kt
 */

fun main() {
    println("=== SimpleTemplateEngine Examples ===\n")
    
    // Example 1: Simple variable substitution
    println("Example 1: Simple Variable Substitution")
    val template1 = "Hello, {{name}}! You have {{count}} messages."
    val data1 = mapOf("name" to "Alice", "count" to 5)
    val result1 = SimpleTemplateEngine.render(template1, data1)
    println("Template: $template1")
    println("Result: $result1")
    println()
    
    // Example 2: Nested property access
    println("Example 2: Nested Property Access")
    val template2 = "User: {{user.name}}, Email: {{user.email}}"
    val data2 = mapOf(
        "user" to mapOf(
            "name" to "Bob",
            "email" to "bob@example.com"
        )
    )
    val result2 = SimpleTemplateEngine.render(template2, data2)
    println("Template: $template2")
    println("Result: $result2")
    println()
    
    // Example 3: Conditional rendering
    println("Example 3: Conditional Rendering")
    val template3 = """
        {{#if loggedIn}}
        Welcome back, {{username}}!
        {{/if}}
    """.trimIndent()
    
    val data3a = mapOf("loggedIn" to true, "username" to "Charlie")
    val result3a = SimpleTemplateEngine.render(template3, data3a)
    println("Template: $template3")
    println("Data (loggedIn=true): $data3a")
    println("Result: '$result3a'")
    
    val data3b = mapOf("loggedIn" to false, "username" to "Charlie")
    val result3b = SimpleTemplateEngine.render(template3, data3b)
    println("Data (loggedIn=false): $data3b")
    println("Result: '$result3b'")
    println()
    
    // Example 4: Loop rendering
    println("Example 4: Loop Rendering")
    val template4 = """
        Shopping List:
        {{#each items}}
        - {{name}}: ${'$'}{{price}}
        {{/each}}
    """.trimIndent()
    
    val data4 = mapOf(
        "items" to listOf(
            mapOf("name" to "Apple", "price" to 1.50),
            mapOf("name" to "Banana", "price" to 0.75),
            mapOf("name" to "Orange", "price" to 2.00)
        )
    )
    val result4 = SimpleTemplateEngine.render(template4, data4)
    println("Result:\n$result4")
    println()
    
    // Example 5: Complex nested structure
    println("Example 5: Complex Nested Structure")
    val template5 = """
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
    
    val data5 = mapOf(
        "user" to mapOf(
            "name" to "Diana",
            "messages" to listOf(
                mapOf("title" to "Hello", "body" to "How are you?"),
                mapOf("title" to "Reminder", "body" to "Meeting at 3pm"),
                mapOf("title" to "Update", "body" to "Project completed")
            )
        )
    )
    val result5 = SimpleTemplateEngine.render(template5, data5)
    println("Result:\n$result5")
}

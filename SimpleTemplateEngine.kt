/**
 * SimpleTemplateEngine - A JSON-oriented Simple Template Engine written in Kotlin
 * 
 * This is a lightweight template engine designed to be included directly in your source code.
 * No build configuration or external dependencies required beyond Kotlin standard library.
 * 
 * Features:
 * - Variable interpolation using {{variable}} syntax
 * - Nested property access using dot notation (e.g., {{user.name}})
 * - JSON-oriented data binding
 * - Conditional rendering with {{#if condition}} ... {{/if}}
 * - Loop rendering with {{#each items}} ... {{/each}}
 * - Simple and easy to use
 * 
 * Usage:
 * ```kotlin
 * val template = "Hello, {{name}}! You have {{count}} messages."
 * val data = mapOf("name" to "Alice", "count" to 5)
 * val result = SimpleTemplateEngine.render(template, data)
 * // Result: "Hello, Alice! You have 5 messages."
 * ```
 * 
 * Advanced Usage with JSON:
 * ```kotlin
 * val template = """
 *     {{#if user}}
 *     Welcome, {{user.name}}!
 *     {{#each user.messages}}
 *     - {{title}}: {{body}}
 *     {{/each}}
 *     {{/if}}
 * """.trimIndent()
 * 
 * val data = mapOf(
 *     "user" to mapOf(
 *         "name" to "Bob",
 *         "messages" to listOf(
 *             mapOf("title" to "Hello", "body" to "How are you?"),
 *             mapOf("title" to "Reminder", "body" to "Meeting at 3pm")
 *         )
 *     )
 * )
 * val result = SimpleTemplateEngine.render(template, data)
 * ```
 * 
 * @author SimpleTemplateEngine Contributors
 * @version 1.0.0
 */
object SimpleTemplateEngine {
    
    /**
     * Render a template with the given data context.
     * 
     * @param template The template string containing placeholders
     * @param data The data context (Map, JSON object, or any value)
     * @return The rendered string
     */
    fun render(template: String, data: Any?): String {
        return renderWithContext(template, data)
    }
    
    /**
     * Internal rendering function with context support.
     */
    private fun renderWithContext(template: String, context: Any?): String {
        var result = template
        var position = 0
        
        while (position < result.length) {
            val startIdx = result.indexOf("{{", position)
            if (startIdx == -1) break
            
            val endIdx = result.indexOf("}}", startIdx)
            if (endIdx == -1) break
            
            val expression = result.substring(startIdx + 2, endIdx).trim()
            
            // Handle control structures
            when {
                expression.startsWith("#if ") -> {
                    val condition = expression.substring(4).trim()
                    val blockEnd = findBlockEnd(result, startIdx, "if")
                    if (blockEnd != -1) {
                        val blockContent = result.substring(endIdx + 2, blockEnd)
                        val conditionValue = getValue(condition, context)
                        val replacement = if (isTruthy(conditionValue)) {
                            renderWithContext(blockContent, context)
                        } else {
                            ""
                        }
                        result = result.substring(0, startIdx) + replacement + 
                                result.substring(blockEnd + "{{/if}}".length)
                        position = startIdx
                        continue
                    }
                }
                expression.startsWith("#each ") -> {
                    val collectionName = expression.substring(6).trim()
                    val blockEnd = findBlockEnd(result, startIdx, "each")
                    if (blockEnd != -1) {
                        val blockContent = result.substring(endIdx + 2, blockEnd)
                        val collection = getValue(collectionName, context)
                        val replacement = if (collection is List<*>) {
                            collection.mapNotNull { item ->
                                renderWithContext(blockContent, item)
                            }.joinToString("")
                        } else {
                            ""
                        }
                        result = result.substring(0, startIdx) + replacement + 
                                result.substring(blockEnd + "{{/each}}".length)
                        position = startIdx
                        continue
                    }
                }
                expression.startsWith("/") -> {
                    // Closing tag, skip
                    position = endIdx + 2
                    continue
                }
                else -> {
                    // Simple variable substitution
                    val value = getValue(expression, context)
                    val replacement = value?.toString() ?: ""
                    result = result.substring(0, startIdx) + replacement + result.substring(endIdx + 2)
                    position = startIdx + replacement.length
                }
            }
        }
        
        return result
    }
    
    /**
     * Find the end of a control structure block.
     */
    private fun findBlockEnd(template: String, startPos: Int, blockType: String): Int {
        val closeTag = "{{/$blockType}}"
        val openTag = "{{#$blockType"
        var depth = 1
        var searchPos = startPos + 1
        
        while (depth > 0 && searchPos < template.length) {
            val nextClose = template.indexOf(closeTag, searchPos)
            val nextOpen = template.indexOf(openTag, searchPos)
            
            when {
                nextClose == -1 -> return -1
                nextOpen == -1 || nextClose < nextOpen -> {
                    depth--
                    if (depth == 0) return nextClose
                    searchPos = nextClose + closeTag.length
                }
                else -> {
                    depth++
                    searchPos = nextOpen + openTag.length
                }
            }
        }
        
        return -1
    }
    
    /**
     * Get a value from the context using a property path.
     * Supports dot notation for nested properties (e.g., "user.name").
     */
    private fun getValue(path: String, context: Any?): Any? {
        if (context == null) return null
        
        val parts = path.split(".")
        var current: Any? = context
        
        for (part in parts) {
            current = when (current) {
                is Map<*, *> -> current[part]
                else -> {
                    // Try to access as a property via reflection
                    try {
                        val method = current?.javaClass?.getMethod("get${part.capitalize()}")
                        method?.invoke(current)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            if (current == null) break
        }
        
        return current
    }
    
    /**
     * Determine if a value is "truthy" for conditional rendering.
     */
    private fun isTruthy(value: Any?): Boolean {
        return when (value) {
            null -> false
            is Boolean -> value
            is String -> value.isNotEmpty()
            is Number -> value.toDouble() != 0.0
            is Collection<*> -> value.isNotEmpty()
            is Map<*, *> -> value.isNotEmpty()
            else -> true
        }
    }
    
    /**
     * Capitalize the first character of a string.
     */
    private fun String.capitalize(): String {
        return if (isEmpty()) this else this[0].uppercase() + substring(1)
    }
}

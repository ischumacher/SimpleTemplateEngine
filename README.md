# SimpleTemplateEngine

A tiny, file-based JSON-driven template engine in Kotlin that favors simplicity, predictability and small footprint over a full templating DSL.

## Overview

SimpleTemplateEngine renders text templates from the filesystem and supports:
- Direct variable substitution using JSON-backed contexts (e.g. `$.title`, `$.user.name`, `$.items[0]`).
- Embedded JSON include wrappers to include and render other templates with their own evaluated context:
  - Include marker: `$.{ ... }` (a JSON object with an `include` key, optional `context`).
  - Example: `$.{"include":"templates/header.html","context":{"title":"$.pageTitle"}}`
- Reuses a standard JSON parser (`org.json.*`) instead of implementing a custom expression language.
- Lightweight API: a single entry point `render(templatePath, context)` returns a fully rendered `String`.

## Key features

- JSON-driven include wrappers: include other templates and pass evaluated contexts (literal values or `$.` references).
- Direct variable references: no braces required for variables; use `$.<path>` syntax.
- Context evaluation: include wrappers may contain nested objects/arrays where `$.` references are resolved against the parent context.
- Include cycle detection: includes that would form cycles throw an `IncludeCycleException`.
- Non-fatal warnings: malformed includes, missing variables, and other non-fatal issues are collected and printed to `stderr` for visibility.
- Minimal API surface: easy to audit, test, and embed into existing Kotlin/Java codebases.

## When to use

- For server-side rendering of small HTML fragments, emails, or simple pages where determinism and transparency are preferred.
- When include contexts are naturally expressed as JSON or assembled programmatically.
- When you want a tiny dependable renderer without advanced templating features.

## Limitations

- Not a full templating language — no control flow, filters or advanced formatting.
- Substitution is textual: JSON values are converted to strings when interpolated.
- Includes require valid JSON for the include wrapper; malformed JSON will produce a warning and skip further include processing in that file.
- Intended for controlled server-side rendering; do not use for untrusted template content without additional sanitization.

## Usage

1. Add `org.json` dependency to your project (Maven/Gradle).
2. Instantiate the engine with the templates root directory and call `render`.

Kotlin example:
```kotlin
import java.io.File
import org.json.JSONObject

val root = File("src/main/resources/templates")
val engine = SimpleTemplateEngine(root)

val ctx = JSONObject().put("pageTitle", "Welcome").put("user",
    JSONObject().put("name", "Alice"))

val output = engine.render("index.html", ctx)
println(output)

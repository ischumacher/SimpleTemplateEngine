# SimpleTemplateEngine

A tiny, file-based JSON-driven template engine in Kotlin that favors simplicity.

## Overview

SimpleTemplateEngine renders text templates from the filesystem and supports:
- Direct variable substitution using JSON-backed contexts (e.g. `$.title`, `$.user.name`, `$.items[0]`).
- Can easily and directly pass data to included templates
- Embedded JSON include wrappers to include and render other templates with their own evaluated context:
  - Include marker: `$.{"include": *template name*, "context": *arbitrary JSON object* }` (a JSON object with an `include` key, optional `context`).
  - Example: `$.{"include":"templates/header.html","context":{"title":"$.pageTitle"}}`
- Reuses a standard JSON parser (`org.json.*`) instead of implementing a custom expression language.
- Lightweight API: a single entry point `render(templatePath, context)` returns a fully rendered `String`.
- Templates can include templates and so on.

## Motivation
I just wanted a simple include mechanism that allowed me to pass data to templates to customize the output.
I didn't really see anything that did this in a way I liked so here we are.

Imagine you have simple header. You want to be able to change it in a single location and have it updated on all your pages.
You also want it to be slightly customized per page. Say it's something like:

```html
<header>
  <h3>You are currently on page $.page.number of article $.page.title </h3>
</header>
```

And then inside your html pages you have:

```html
<body>
  $.{"include":"/template/header.html", "context": {"page":{"number":737, "title":"Quantum-Entangled Hyperflux Modulation in Bioadaptive Nanoplasmonic Metamaterials for Transdimensional Neurocryptographic Applications"}}}
<p>I hope you enjoyed the simplified introduction, because now things start to get complicated. ...</p>
...
</body>
```

Kotlin example:
```kotlin
import java.io.File
import org.json.JSONObject

val root = File("src/main/resources/templates")
val engine = SimpleTemplateEngine(root)

val ctx = JSONObject("""
{
  "pageTitle": "Welcome",
  "user": "Alice"
}
""")

val output = engine.render("index.html", ctx)
println(output)
```

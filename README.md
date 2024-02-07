# Calico DOM Utilities

This project, Calico DOM Utilities, 
is a comprehensive toolkit designed to facilitate unit testing in the Calico environment. 
It offers utilities that are specifically tailored to interact with and test the individual components of Calico.

The structure and approach of our unit tests draw inspiration from the unit cases of Laminar, 
a Scala library for building reactive user interfaces.

Calico is built upon the `cats-effect` library, which is a powerful tool for writing asynchronous, concurrent, and resource-safe code in Scala. 
Basic test cases written using  `munit-cats-effect` can be found in `with_munit` branch.

The main branch contains test cases based on `scalatest` and `cats-effect-testing-scalatest` libraries.

## `munit-cats-effect` vs `cats-effect-testing-scalatest`
### Fixtures 
MUnit supports reusable suite-local fixtures that are instantiated only once for the entire test suite. 
This is useful when an expensive resource (like an HTTP client) is needed for each test case, but it is undesirable to allocate a new one each time.
Scalatest does not support this feature. 
### Failure messages
Munit is not able to print the failures in the test cases when using PlayWright. 
Hence, you will find the recent test cases are developed using scalatest. 

## Testing Events 
Observing for events using scalajs-dom is straightforward similar to the way we in javascript.
```scala
input.addEventListener[dom.Event](
        "input",
        (ev: dom.Event) => {
          span.textContent = input.value.toUpperCase
        })

```
The above event can be asserted using scalatest as follows:
```scala
      input.value = "one"
      input.dispatchEvent(new dom.Event("input"))
      span.textContent should equal("ONE")
```
Calico starts processing events only after the rendering is completed. 
In a unit test case it is not possible to wait for the rendering to complete.
To resolve this issue we have to use `IO.cede`.  
This will temporarily yield control flow back to the browser so that it may re-render the UI, before resuming the task.
`IO.cede` has to be inserted in to the rendering sequences. To insert multiple times use `IO.cede.replicateA_(10)`.

```scala
input_span_component.mountInto(rootElement).surround {
      IO.cede.replicateA_(10) *>
        IO {
          val inputChangeEvent = new dom.InputEvent(
            "input",
            new InputEventInit {
              data = "Ram"
            })
          val actualInput =
            document.querySelector("#app > div > input").asInstanceOf[dom.html.Input]
          actualInput.value = "Ram"
          actualInput.addEventListener[dom.InputEvent](
            "input",
            listener = (ev: dom.InputEvent) => {
              println(s"input event fired with data: ${ev.data}") // Printing after assertion
            })
          actualInput.dispatchEvent(inputChangeEvent)
        } *> IO.cede.replicateA_(10) *> IO {
          val actualSpan =
            document.querySelector("#app > div > span").asInstanceOf[dom.html.Span]
          actualSpan.textContent should equal(" Hello, RAM")
        }
    }
```
Note in above test case cede is needed after `surround` and after `actualInput.dispatchEvent(inputChangeEvent)`.


Stay tuned for more updates and enhancements to our utilities. 
Your feedback and contributions are always welcome!
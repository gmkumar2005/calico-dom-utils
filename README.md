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



Stay tuned for more updates and enhancements to our utilities. 
Your feedback and contributions are always welcome!
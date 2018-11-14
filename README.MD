# Jong Soft: Java Language Extensions
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jongsoft.lang/language/badge.svg)
[![CircleCI](https://circleci.com/bb/jongsoftdev/language-extension.svg?style=svg)](https://circleci.com/bb/jongsoftdev/language-extension)
[![Coverage Status](https://coveralls.io/repos/bitbucket/jongsoftdev/language-extension/badge.svg?branch=master)](https://coveralls.io/bitbucket/jongsoftdev/language-extension?branch=master)

With Java 8 a great new introduction was made to support functional programming. Unfortunately true functional programming was not
yet supported.

Now fully supports the Java modules introduced in Java 9.

## Module structure
The Jong Soft language extensions is build up out of several Maven modules that can be included independent of each other.
Each module expands part of the default Java 8 library.

### Language Commons
The commons module contains interfaces supporting a more declarative way of programming. It consists out of the needed interfaces
for all the other modules. Like:

* `Builder`, a base to create immutable entities through the builder pattern
* `Value`, base interface for all entities within the extension framework
* `Runner`, a variation of the `Runnable` interface
* `CheckedConsumer`, `CheckedFunction`, `CheckedRunner`, `CheckedSupplier`

To include this module use the Maven dependency below:

    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>commons</artifactId>
        <version>0.0.2-SNAPSHOT</version>
    </dependency>

### Language Collections

The collections module consists out of the following immutable list implementations:

* `Array`, a wrapping entity around an primitive array
* `TailedList`, a linked list structure
* `Tuple`, a tuple implementation to store 2 or more entities

To include this module use the Maven dependency below:

    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>collections</artifactId>
        <version>0.0.2-SNAPSHOT</version>
    </dependency>

### Language Functional

The functional model includes the following implementations:

* `Optional`, a purely functionally version of the Optional introduced in Java 8
* `Try`, a functional implementation to wrap exception handling

To include this module use the Maven dependency below:

    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>functional</artifactId>
        <version>0.0.2-SNAPSHOT</version>
    </dependency>

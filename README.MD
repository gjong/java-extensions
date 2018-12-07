# Jong Soft: Java Language Extensions
![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jongsoft.lang/language/badge.svg)
[![CircleCI](https://circleci.com/bb/jongsoftdev/language-extension/tree/master.svg?style=svg)](https://circleci.com/bb/jongsoftdev/language-extension/tree/master)
[![Coverage Status](https://coveralls.io/repos/bitbucket/jongsoftdev/language-extension/badge.svg?branch=master)](https://coveralls.io/bitbucket/jongsoftdev/language-extension?branch=master)
[![SonarCloud Quality](https://sonarcloud.io/api/project_badges/measure?project=com.jongsoft.lang%3Alanguage&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.jongsoft.lang%3Alanguage)

With Java 8 a great new introduction was made to support functional programming. Unfortunately true functional programming was not
yet supported.

Now fully supports the Java modules introduced in Java 9.

### Functional classes
The commons module contains interfaces supporting a more declarative way of programming. It consists out of the needed interfaces
for all the other modules. Like:

* `Builder`, a base to create immutable entities through the builder pattern
* `Value`, base interface for all entities within the extension framework
* `Optional`, a purely functionally version of the Optional introduced in Java 8
* `Try`, a functional implementation to wrap exception handling
* `Runner`, a variation of the `Runnable` interface
* `CheckedConsumer`, `CheckedFunction`, `CheckedRunner`, `CheckedSupplier`

### Collection based classes
* `Sequence`, access to an ordered collection
* `Set`, a collection with unique entries based on the hash code
* `SortedSet`, a collection with unique entries based on the hash code
* `Tuple`, a tuple implementation to store 2 or more entities
* `Map`, a map implementation

## Maven usage
To include this module use the Maven dependency below:

    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>language</artifactId>
        <version>0.0.4-SNAPSHOT</version>
    </dependency>

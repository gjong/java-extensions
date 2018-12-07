![Maven Central](https://img.shields.io/maven-central/v/com.jongsoft.lang/language.svg?color=green&style=flat-square)
[![SonarCloud Quality](https://sonarcloud.io/api/project_badges/measure?project=com.jongsoft.lang%3Alanguage&metric=alert_status&?style=flat-square)](https://sonarcloud.io/dashboard?id=com.jongsoft.lang%3Alanguage)
![CircleCI](https://img.shields.io/circleci/project/bitbucket/jongsoftdev/language-extension/master.svg?style=flat-square)
[![Coveralls bitbucket branch](https://img.shields.io/coveralls/bitbucket/jongsoftdev/language-extension/master.svg?style=flat-square)](https://coveralls.io/bitbucket/jongsoftdev/language-extension?branch=master)
![APMLicense](https://img.shields.io/apm/l/vim-mode.svg?style=flat-square)

# Java Language Extensions
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

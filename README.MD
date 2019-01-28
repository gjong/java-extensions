# Java Language Extensions

[![Maven Central](https://img.shields.io/maven-central/v/com.jongsoft.lang/language.svg?color=green&style=flat-square)](https://search.maven.org/artifact/com.jongsoft.lang/language/)
[![SonarCloud Quality](https://sonarcloud.io/api/project_badges/measure?project=com.jongsoft.lang%3Alanguage&metric=alert_status&?style=flat-square)](https://sonarcloud.io/dashboard?id=com.jongsoft.lang%3Alanguage)
![CircleCI](https://img.shields.io/circleci/project/bitbucket/jongsoftdev/language-extension/master.svg?style=flat-square)
[![Coveralls bitbucket branch](https://img.shields.io/coveralls/bitbucket/jongsoftdev/language-extension/master.svg?style=flat-square)](https://coveralls.io/bitbucket/jongsoftdev/language-extension?branch=master)
![APMLicense](https://img.shields.io/apm/l/vim-mode.svg?style=flat-square)

With Java 8 a great new introduction was made to support functional programming. Unfortunately true functional programming was not
yet supported.

**Note:** from version 0.0.4 onwards only Java 11+ is supported

## Features in the library
The library currently has the following groups of features that it adds on top of the default Java 11+ system:

* Functional interfaces, allowing for a more functional and declarative way of programming
* Immutable collections, supporting better declarative and safer operations

### Functional classes
The commons module contains interfaces supporting a more declarative way of programming. It consists out of the needed interfaces
for all the other modules. Like:

* `Value`, base interface for all entities within the extension framework
* `Optional`, a purely functionally version of the Optional introduced in Java 8
* `Try`, a functional implementation to wrap exception handling
* `CheckedConsumer`, `CheckedFunction`, `CheckedRunner`, `CheckedSupplier`

### Collection based interfaces
* `Sequence`, access to an ordered collection
* `Set`, a collection with unique entries based on the hash code
* `SortedSet`, a collection with unique entries based on the hash code
* `Tuple`, a tuple implementation to store 2 or more entities
* `Map`, a map implementation

## Documentation
To support development work the JavaDoc is published online for this library. Currently the following documentation is
published:
    
* [Production API Documentation](https://jdocstorage.z6.web.core.windows.net/java/language/com.jongsoft.lang/module-summary.html)
* [Snapshot API Documentation](https://jdocstorage.z6.web.core.windows.net/java/language/snapshot/index.html)

Most of the functional interfaces can be instantiated using the [API](https://jdocstorage.z6.web.core.windows.net/java/language/com.jongsoft.lang/com/jongsoft/lang/API.html)
class.

**Example 1: using a declarative sequence of strings**
```java
class Main {
    public static void main(String[] args){
        // Instantiate a list, filter the strings by length and remove one
        Sequence<String> myStrings = API.List("test", "string", "one")
           .filter(s -> s.length() == 3)
           .remove("test");
        
        // The result will be: Sequence[one] 
        System.out.println(myStrings.toString());
    }
}
``` 

## Maven usage
To include this module use the Maven dependency below:

```xml
    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>language</artifactId>
        <version>0.0.5</version>
    </dependency>
```

## License
The MIT License (MIT)
Copyright (c) 2016-2019 Jong Soft Development

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
  to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

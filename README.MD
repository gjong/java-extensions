# Java Language Extensions

[![Maven Central](https://img.shields.io/maven-central/v/com.jongsoft.lang/language.svg?color=green&style=flat-square)](https://search.maven.org/artifact/com.jongsoft.lang/language/)
[![SonarCloud Quality](https://sonarcloud.io/api/project_badges/measure?project=com.jongsoft.lang%3Alanguage&metric=alert_status&?style=flat-square)](https://sonarcloud.io/dashboard?id=com.jongsoft.lang%3Alanguage)
![CircleCI](https://img.shields.io/circleci/project/bitbucket/jongsoftdev/language-extension/master.svg?style=flat-square)
[![Coveralls bitbucket branch](https://img.shields.io/coveralls/bitbucket/jongsoftdev/language-extension/master.svg?style=flat-square)](https://coveralls.io/bitbucket/jongsoftdev/language-extension?branch=master)
![APMLicense](https://img.shields.io/apm/l/vim-mode.svg?style=flat-square)

With the introduction of Java 8 the first steps were taken to support functional programming. Unfortunately true functional programming is not
yet supported.

**Note:** from version 1.1.0 the API class became deprecated

## Features in the library
The library currently has the following groups of features that it adds on top of the default Java 11+ system:

* Functional interfaces, allowing for a more functional and declarative way of programming
* Immutable collections, supporting better declarative and safer operations

### Functional classes
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

### Date / Time based interfaces
* `Range`, a wrapper to create a date or date time range

## Documentation
To aid in using this library the JavaDoc is published online. This documentation contains information about the various 
interfaces present. The documentation is published at:
    
* [Production API Documentation](https://jdocstorage.z6.web.core.windows.net/java/language/com.jongsoft.lang/module-summary.html)
* [Snapshot API Documentation](https://jdocstorage.z6.web.core.windows.net/java/language/snapshot/index.html)

## Examples

### Collection examples

#### Example 1: using a declarative sequence of strings
```java
import com.jongsoft.lang.Collections;

class Main {
    public static void main(String[] args){
        // Instantiate a list, filter the strings by length and remove one
        Sequence<String> myStrings = Collections.List("test", "string", "one")
           .filter(s -> s.length() == 3)
           .remove("test");
        
        // The result will be: Sequence[one] 
        System.out.println(myStrings.toString());
    }
}
```

#### Example 1: creating a distinct list
```java
import com.jongsoft.lang.Collections;

class Main {
    public static class Entity {
       private final String value;
       
       public Entity(String value) {
          this.value = value;
       }

       public boolean contains(String other) {
          return value.contains(other);
       }
    }

    public static void main(String[] args){
        // Instantiate a list, filter the strings by length and remove one
        Sequence<String> myStrings = Collections.List(
                new Entity("sample"),
                new Entity("more sample")
           )
           .distinctBy((e1, e2) => e1.contains(e2));
        
        // The result will be: Sequence["sample"] 
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
        <version>1.1.0</version>
    </dependency>
```

## License
The MIT License (MIT)
Copyright (c) 2016-2020 Jong Soft Development

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
  to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

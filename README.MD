# Jong Soft: Java Language Extensions

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
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

### Language Collections

To include this module use the Maven dependency below:

    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>collections</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

### Language Functional

To include this module use the Maven dependency below:

    <dependency>
        <groupId>com.jongsoft.lang</groupId>
        <artifactId>functional</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
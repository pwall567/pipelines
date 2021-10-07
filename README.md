# pipelines

[![Build Status](https://travis-ci.com/pwall567/pipelines.svg?branch=master)](https://travis-ci.com/github/pwall567/pipelines)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.util/pipelines?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.util%22%20AND%20a:%22pipelines%22)

Pipeline library for Java.

This library enables the construction of "pipelines", in which data (frequently in the form of `int` values) is pumped
in at one end and values are emitted at the other.
The conversion may be one-to-one, one-to-many or many-to-one.

A frequent use is for character encoding (conversion of bytes to characters and _vice versa_) and several classes are
provided to perform such conversions.

## Background

This project was born from a requirement for a mechanism to process a stream of characters, outputting results as they
became available.
The specific task was to process JSON data as each element was completed, without waiting until the entire input had
been read into a buffer.

But the JSON data in this case was not in character form; it was a stream of bytes.
That meant decoding from a character set (usually UTF-8) &ndash; another many-to-one pipeline process.
Thus was born the generalised pipeline library &ndash; a simple mechanism for connecting the output of one process to
input of the next, without concern for the one-to-many or many-to-one characteristics of any step in the chain.

The JSON pipeline ends up looking something like this:

&emsp;_bytes_&emsp;&rArr;&emsp;{ **UTF-8 decoder** }&emsp;&rArr;&emsp;_characters_&emsp;&rArr;&emsp;{ **JSON processor** }&emsp;&rArr;&emsp;_objects_

I considered using the Java `Consumer` and `IntConsumer` interfaces for the objects that accept input from the original
source or from the previous element of the pipeline, but I found I needed additional functionality beyond the simple
`accept` function those interfaces provide.
So this library defines an `Acceptor` interface; the `Pipeline` interface extends `Acceptor` and adds an `emit` function
to forward data to the downstream object.

## Concepts

### Parameterised Types

The classes and interfaces use the following naming convention for their parameterised types:

- `<A>`: the type that the class/interface accepts
- `<E>`: the type that the class/interface emits
- `<R>`: the "result" type (the pipeline may have a result type which gets propagated to the first element of the
pipeline; instances that don't need a result can use `Void` &ndash; `Unit` in Kotlin)

### Acceptor interface

This defines a small number of functions:

- `accept(value)`: accept a value (the implementation in `AbstractAcceptor` tests for `null` as an end-of-data marker,
and invokes the abstract method `acceptObject()` for non-null values)
- `close()`: inherited from the `AutoCloseable` interface (unfortunately, because the inherited signature specifies a
checked exception, many of the method signatures in the library also include this exception)
- `isClosed()`: returns `true` if the acceptor is closed
- `getResult()`: returns the result (the default implementation throws an exception)
- `isComplete()`: returns `true` if all many-to-one sequences are complete, for example, all the bytes in a UTF-8
multi-byte sequence have been received
- `flush()`: instructs buffering acceptors to flush data to their output

### Pipeline interface

`Pipeline` extends `Acceptor` and adds:

- `emit(value)`: emit a value

### IntXxxx classes and interfaces

Because Java treats `int` more efficiently than the boxing class `Integer`, there are `IntXxxx` variants of many of the
classes and interfaces.
And because many uses of `int` treat -1 as an end-of-data marker, this library also considers -1 as signifying end of
data.
That means that the `IntXxxx` classes include an `accept()` method that takes an `int`, performs the end-of-data test
and then calls an `acceptInt()` abstract method that must be overridden by the implementing class.

### AbstractXxxx classes

These provide default implementations for much of the functionality; implementing classes should generally derive from
these classes.

## Character Set Encoding and Decoding

The library includes several character set encoding and decoding classes, all of them implementing the `IntPipeline`
interface.

| Class                   | Accepts             | Emits               |
| ----------------------- | ------------------- | ------------------- |
| `UTF8_CodePoint`        | UTF-8               | Unicode code points |
| `UTF16_CodePoint`       | UTF-16              | Unicode code points |
| `ISO8859_1_CodePoint`   | ISO-8859-1          | Unicode code points |
| `ISO8859_15_CodePoint`  | ISO-8859-15         | Unicode code points |
| `Windows1252_CodePoint` | Windows-1252        | Unicode code points |
| `ASCII_CodePoint`       | ASCII               | Unicode code points |
| `CodePoint_UTF8`        | Unicode code points | UTF-8               |
| `CodePoint_UTF16`       | Unicode code points | UTF-16              |
| `CodePoint_ISO8859_1`   | Unicode code points | ISO-8859-1          |
| `CodePoint_ISO8859_15`  | Unicode code points | ISO-8859-15         |

Unicode code points are 32-bit quantities containing the full range of Unicode values; UTF-16 refers to the 16-bit
version of Unicode, with pairs of surrogate characters representing characters outside the "Basic Multilingual Plane".
Because Java mostly works with 16-bit characters, the `CodePoint_UTF16` pipeline will frequently be needed in
combination with the other classes.

### `SwitchableDecoder` and `DynamicDecoder`

Of particular interest are the `SwitchableDecoder` and `DynamicDecoder` classes.
`SwitchableDecoder` is designed for the case where the file itself contains the character set specification &ndash; for
example, an XML file starts with a prolog line:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<example>data</example>
```

`SwitchableDecoder` starts reading the file in ASCII mode, ASCII being the common subset of most of the generally-used
character sets.
When the reading process recognises an explicit character set definition, it can switch to the specified character set.

`DynamicDecoder` takes this a step further &ndash; it attempts to determine the character set from the content of the
input stream.
In some cases, such as UTF-16LE (little-endian) or UTF-16BE (big-endian), examination of the first few bytes will allow
the decoder to make a quick determination.
But for UTF-8, ISO-8859-1 and Windows-1252, which account for the majority of character encoding in the English-speaking
world, the most frequently-used characters are identical, and it is only when a special character is encountered that
the decoder can attempt to infer the character set being used.

`DynamicDecoder` will check whether a special character sequence is valid UTF-8, and if so, it wil switch to that
character set for the remainder of the stream.
If the character sequence is not valid UTF-8, the decoder will switch to Windows-1252 (which is in effect a superset of
ISO-8859-1, so that character set will be covered too).

Since `DynamicDecoder` extends `SwitchableDecoder`, the ability to switch explicitly on receipt of an indication in the
text is always possible.

### `DynamicReader`

To simplify the use of `DynamicDecoder`, `DynamicReader` implements the `Reader` interface, applying the
`DynamicDecoder` to a supplied `InputStream`.

## Class Diagram

The collection of classes rapidly grew to a large number, and to keep track of the whole library a class diagram is
helpful.

The following is a class diagram in UML form &ndash; or rather, a rough approximation of UML.
In this diagram, the same rectangular block is used to represent an `interface` and a `class`.
An `interface`, like an `abstract class`, has its name in _italics_.

A solid line indicating inheritance is used both for both an `extends` relationship and a primary `implements`
relationship.
A dotted line represents a secondary `implements` relationship, where a class both extends a base class and implements
an interface.
Lines that cross do not interact.

![Class Diagram](doc/dia/pipeline30.png "UML Class Diagram")

The diagram was produced by [Dia](https://wiki.gnome.org/Apps/Dia/); the diagram file is at
[doc/dia/pipeline30.dia](doc/dia/pipeline30.dia).

## Example

To accept bytes in UTF-8 and aggregate them into a `String`:

```Java
public class ReadString {

    public String read(InputStream inputStream) {
        IntPipeline<String> pipe = new UTF8_CodePoint<>(new CodePoint_UTF16<>(new StringAcceptor()));
        while (!pipe.isClosed())
            pipe.accept(inputStream.read());
        return pipe.getResult();
    }

    public static class StringAcceptor extends AbstractIntAcceptor<String> {
        private StringBuilder sb = new StringBuilder();
        @Override
        public void acceptInt(int value) {
            sb.append((char)value);
        }
        @Override
        public String getResult() {
            return sb.toString();
        }
    }

}
```

Of course, all of this is also accessible from Kotlin:

```Kotlin
    fun readString(inputStream: InputStream): String {
        val pipe = UTF8_CodePoint(CodePoint_UTF16(StringAcceptor()))
        while (!pipe.isClosed)
            pipe.accept(inputStream.read())
        return pipe.result
    }

    class StringAcceptor : AbstractIntAcceptor<String>() {
        private val sb = StringBuilder()
        override fun acceptInt(value: Int) {
            sb.append(value.toChar())
        }
        override fun getResult() = sb.toString()
    }
```

## Dependency Specification

The latest version of the library is 3.0, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>net.pwall.util</groupId>
      <artifactId>pipelines</artifactId>
      <version>3.0</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'net.pwall.util:pipelines:3.0'
```
### Gradle (kts)
```kotlin
    implementation("net.pwall.util:pipelines:3.0")
```

Peter Wall

2021-10-07

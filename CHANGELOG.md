# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [3.1] - 2021-10-14
### Changed
- `DynamicDecoder`, `DynamicReader`: added ability to pre-select encoding

## [3.0] - 2021-10-07
### Added
- `EncodingPipeline`: encoder for 8-bit character sets
- `DynamicDecoder`: decoder that chooses the mapping based on data content
- `DynamicReader`: an implementation of `Reader` that uses `DynamicDecoder`
- `ByteArrayAcceptor`: accumulates characters into a byte array
- `AppendableAcceptor`: forwards characters to an `Appendable`
- `Pipelines`: static functions
- several new encoder and decoder classes
### Changed
- `BaseAcceptor`: added default implementation for `getResult()`
- `AbstractAcceptor`, `AbstractIntAcceptor`: added `vararg` versions of `accept()`

## [2.1] - 2021-08-04
### Added
- `URIEncoder`, `URIDecoder`: new encode/decode classes

## [2.0] - 2021-08-03
### Added
- `BasePipeline`: new interface implemented by all pipelines
### Changed
- pipeline classes: implement new interface (possible breaking change)
- `BaseAbstractAcceptor`: removed `getResult()` (it was always overridden)
- `Fold`: bug fix (didn't return result)

## [1.0] - 2021-08-02
### Added
- `ObjectIntPipeline`, `AbstractObjectIntPipeline`
- several utility classes
### Changed
- All: implemented new package layout, with subpackages for codec, buffer etc. (**BREAKING CHANGE**)
- `Acceptor`, `IntAcceptor`: added/modified helper methods (**BREAKING CHANGE** - helper method functionality change)

## [0.9] - 2020-05-01
### Changed
- `BaseAcceptor`: added `flush()`
- `AbstractPipeline`, `AbstractIntPipeline`, `AbstractIntObjectPipeline`: added `flush()`

## [0.8] - 2020-02-27
### Changed
- Improved documentation
- `IntAcceptor`: added accept `InputStream`

## [0.7] - 2020-02-10
### Added
- `SwitchableDecoder`: Allows decoder to switch according to content
### Changed
- Moved code from `AbstractAcceptor` and `AbstractIntAcceptor` to `BaseAbstractAcceptor`
- `IntAcceptor`: added accept byte range

## [0.6] - 2020-01-20
### Added
- `ListAcceptor`, `SetAcceptor` and `StringAcceptor`
### Changed
- Modified parameterised types on constructors etc. (use wildcards)

## [0.5] - 2020-01-15
### Changed
- Changed pipelines to propagate "complete" state
- Added default `getResult` method to `AbstractXxxAcceptor` - throws `UnsupportedOperationException`
- Minor additions to JavaDoc

## [0.4] - 2020-01-14
### Changed
- Minor changes to decoding pipelines
### Added
- `DecoderFactory`: Creates decoder for given charset
- `IntObjectPipeline` and `AbstractIntObjectPipeline`

## [0.3] - 2020-01-13
### Changed
- `IntAcceptor`: Added default methods to take string and byte array

## [0.2] - 2020-01-12
### Changed
- Major changes, including parameterised return types

## [0.1] - 2020-01-10
### Added
- Initial versions of all files

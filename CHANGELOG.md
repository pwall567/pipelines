# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
### Added
- `ObjectIntPipeline`, `AbstractObjectIntPipeline`
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

# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [0.6] - 2020-01-20
### Changed
- Modified parameterised types on constructors etc. (use wildcards)
- `ListAcceptor`, `SetAcceptor` and `StringAcceptor`

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

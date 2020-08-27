# [2.1.0](https://github.com/Silthus/ebean-wrapper/compare/v2.0.0...v2.1.0) (2020-08-27)


### Bug Fixes

* bypass DriverManager unwilling to load driver from external jar ([bec95ea](https://github.com/Silthus/ebean-wrapper/commit/bec95ead36f9f9d8585634bf3778ed1559ec2c77))


### Features

* add option to pass a list of entity classes ([bcb3217](https://github.com/Silthus/ebean-wrapper/commit/bcb3217866d5cbb02f0ae7b6b294fcb2513289ca))

# [2.0.0](https://github.com/Silthus/ebean-wrapper/compare/v1.1.0...v2.0.0) (2020-08-26)


### Features

* refactor EbeanWrapper to take a full ebean config ([ac87138](https://github.com/Silthus/ebean-wrapper/commit/ac871384639ca678a02814e0ebb683881ab7a148))


### BREAKING CHANGES

* the EbeanWrapper now takes a Config parameter containing all of the needed configuration for the wrapper. Including a full ebean database and/or datasource config.

# [1.1.0](https://github.com/Silthus/ebean-wrapper/compare/v1.0.0...v1.1.0) (2020-08-25)


### Bug Fixes

* driver file is created as directory ([b54c533](https://github.com/Silthus/ebean-wrapper/commit/b54c533eff92ee6c703ba8b15c01879cedc84313))
* wrong h2 driver download url ([dd785f2](https://github.com/Silthus/ebean-wrapper/commit/dd785f299c245386e4c4c82c6faf1a27707c0553))


### Features

* allow customizing the driver location ([fd27b3d](https://github.com/Silthus/ebean-wrapper/commit/fd27b3dba510ba3be522aabdb61b2e60787f7f97))

# 1.0.0 (2020-08-25)


### Features

* create EbeanWrapper with driver download functionality ([c12b7e3](https://github.com/Silthus/ebean-wrapper/commit/c12b7e3cdf7bce0de6c26dc72a78bff652e7b4ed))

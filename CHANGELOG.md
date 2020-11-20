## [2.8.2](https://github.com/Silthus/ebean-wrapper/compare/v2.8.1...v2.8.2) (2020-11-20)


### Bug Fixes

* **db:** store all entities in a global variable ([b907564](https://github.com/Silthus/ebean-wrapper/commit/b9075649f221f7a6b56b8e8f6e061cdffce0fbc3))

## [2.8.1](https://github.com/Silthus/ebean-wrapper/compare/v2.8.0...v2.8.1) (2020-11-20)


### Bug Fixes

* **deps:** rollback to ebean 12.5.2 ([d5e8edd](https://github.com/Silthus/ebean-wrapper/commit/d5e8edd18bae3e5f23381186d2babad61583b643))

# [2.8.0](https://github.com/Silthus/ebean-wrapper/compare/v2.7.0...v2.8.0) (2020-11-20)


### Bug Fixes

* **ebean:** add entity classes instead of setting them ([4a619a0](https://github.com/Silthus/ebean-wrapper/commit/4a619a0777371208750113bcb5fe7de41aacb062))
* **ebean:** only set entities if classes are provided ([bb067b9](https://github.com/Silthus/ebean-wrapper/commit/bb067b9731d7213f49c8119ba37cdd1d479cbd26))


### Features

* **migrations:** add option to specify migration table ([f91f489](https://github.com/Silthus/ebean-wrapper/commit/f91f4895106446dea110744367524a8c3e004e5f))

# [2.7.0](https://github.com/Silthus/ebean-wrapper/compare/v2.6.1...v2.7.0) (2020-11-17)


### Features

* **bukkit:** add bukkit config builder with simple defaults ([f774d22](https://github.com/Silthus/ebean-wrapper/commit/f774d2233ff6a676f5df3b0b3a47fb9417b9637e))

## [2.6.1](https://github.com/Silthus/ebean-wrapper/compare/v2.6.0...v2.6.1) (2020-11-14)


### Bug Fixes

* **migrations:** do not copy migrations if running in test scope ([a476977](https://github.com/Silthus/ebean-wrapper/commit/a476977dcbe8be4878c60718bb237255c01fdb5f))

# [2.6.0](https://github.com/Silthus/ebean-wrapper/compare/v2.5.0...v2.6.0) (2020-11-09)


### Features

* add base entity convenience class ([2bd2ed7](https://github.com/Silthus/ebean-wrapper/commit/2bd2ed7819624dcf41dab100c2581aa1efc696dd))

# [2.5.0](https://github.com/Silthus/ebean-wrapper/compare/v2.4.0...v2.5.0) (2020-11-07)


### Features

* add convenience Driver class with mappings ([41e5a23](https://github.com/Silthus/ebean-wrapper/commit/41e5a23847385d5737aa221bb38ae42cec8727c6))

# [2.4.0](https://github.com/Silthus/ebean-wrapper/compare/v2.3.2...v2.4.0) (2020-11-07)


### Features

* simplify defaults for spigot ([4c35373](https://github.com/Silthus/ebean-wrapper/commit/4c353736f79fdaf1b4fb0562088f374d32894465))

## [2.3.2](https://github.com/Silthus/ebean-wrapper/compare/v2.3.1...v2.3.2) (2020-11-04)


### Bug Fixes

* **migration:** append driver identifier to migration path ([6bd214c](https://github.com/Silthus/ebean-wrapper/commit/6bd214c6d3ff9f312cc94c60f52042228919671e))

## [2.3.1](https://github.com/Silthus/ebean-wrapper/compare/v2.3.0...v2.3.1) (2020-11-04)


### Bug Fixes

* copy dbmigration from using class to temp dir and execute from there ([65711a3](https://github.com/Silthus/ebean-wrapper/commit/65711a3c6ea63342d40087098484181635cee08d))

# [2.3.0](https://github.com/Silthus/ebean-wrapper/compare/v2.2.0...v2.3.0) (2020-11-04)


### Features

* expose ebean-wrapper as a spigot plugin ([b916329](https://github.com/Silthus/ebean-wrapper/commit/b9163290739497a8c2ccd49f6734801c329186b8))

# [2.2.0](https://github.com/Silthus/ebean-wrapper/compare/v2.1.0...v2.2.0) (2020-09-04)


### Features

* shade the h2 library ([3db24ff](https://github.com/Silthus/ebean-wrapper/commit/3db24ffc75c9b1b01c89e310536c7d0036a310fd))

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

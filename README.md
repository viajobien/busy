# busy

[![Build Status](https://travis-ci.org/viajobien/busy.svg?branch=master)](https://travis-ci.org/viajobien/busy)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Coverage Status](https://coveralls.io/repos/github/viajobien/busy/badge.svg?branch=master)](https://coveralls.io/github/viajobien/busy?branch=master)

**Work in progress**

## Notes about current release

First of all, the very first release is 0.1.0. It has the only purpose to configure the publish to maven central.
It is not fully tested, so, there is no guarantees it is working properly.

## What is "busy" about?

A time ago, in [Viajobien.com](http://www.viajobien.com), as was expected, the number of applications and services started to grow.
So, to reuse this services, we choose to use an ESB (Enterprise Service Bus) and we implemented our own.    
Why not to use something like Mule ESB, WSO2 ESB or Apache ServiceMix? There are three reasons. First, we wanted to learn about integrations;
second, we didn't need complex data adapters, the majority of the services were ours and for those that not, we already had some application to adapt the data;
third, and not less important, for existing solutions, is very difficult to add new routes or services to the BUS (a new deploy or use OSGi, and it is a pain... you know).

Resuming what is this project about, it's a helper to create a service bus based on an application running [Play! Framework](https://www.playframework.com/) and [Scala](http://scala-lang.org/).

## Documentation

For documentation you can see the [wiki](https://github.com/viajobien/busy/wiki) and our [example](https://github.com/viajobien/busy-example).

### Quick Start

**_It's required to have knowledge on [Scala](http://scala-lang.org/), [Play! Framework](https://www.playframework.com/) and [sbt](http://www.scala-sbt.org/)._**

Create a new sbt project with Play+Scala and add the following dependency on your build.sbt:

```
libraryDependencies += "com.viajobien" %% "busy" % "0.1.0"
```


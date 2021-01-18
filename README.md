# DSB #

DSB stands for *dirt simple builder.*

## Overview ##
Dsb exists to provide a very simple Java build tool. Typical Java projects conform to a conventional directory structure, and don't require
a lot of custom tooling or scripting in their build. However, the build tool options 
that exist -- ant, gradle, and maven, for example -- are overly complex for simple projects. Your project may
outgrow dsb, at which point the complexity of those tools may become necessary.

The goals of dsb are:
1. Avoid configuration entirely when a project's structure and build steps are simple
1. Allow for simple, human readable configuration when the build is slightly more complicated
1. Be a self-contained, downloadable tool that does not need third-party libraries to function.


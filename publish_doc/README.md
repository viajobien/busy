# Publish to SonaType

## Some useful links

http://www.scala-sbt.org/release/docs/Using-Sonatype.html
http://www.scala-sbt.org/sbt-pgp/usage.html
https://github.com/xerial/sbt-sonatype
https://issues.sonatype.org/browse/OSSRH-25611

## Generate GPG key

    $ sbt
    > set pgpReadOnly := false
    > pgp-cmd gen-key
    The name associated: viajobien.com
    Enter an email and then the passphrase

## Load public key

Get the content of `~/.sbt/gpg/pubring.asc` and paste on http://pgp.mit.edu:11371/.

## Publishing

    $ sbt publishSigned (this will ask for the passphrase)
    $ sbt sonatypeRelease


import com.jsuereth.sbtpgp.PgpKeys

lazy val sharedSettings = Seq(
  organization := "com.github.dwickern",
  scalaVersion := "2.13.0",
  crossScalaVersions := Seq(scalaVersion.value, "2.12.1"),
  libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.1.0" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  publishTo := sonatypePublishToBundle.value,
  pomExtra := {
    <url>https://github.com/dwickern/scala-nameof</url>
    <licenses>
      <license>
        <name>MIT license</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/dwickern/scala-nameof.git</connection>
      <developerConnection>scm:git:git@github.com:dwickern/scala-nameof.git</developerConnection>
      <url>github.com/dwickern/scala-nameof.git</url>
    </scm>
    <developers>
      <developer>
        <id>dwickern</id>
        <name>Derek Wickern</name>
        <url>https://github.com/dwickern</url>
      </developer>
    </developers>
  }
)

releaseCrossBuild := true
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
}

lazy val root = project.in(file("."))
  .aggregate(nameofJVM, nameofJS)
  .settings(sharedSettings: _*)
  .settings(
    publish := {},
    publishLocal := {},
    PgpKeys.publishSigned := {}
  )

lazy val nameof = crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure).in(file("."))
  .settings(sharedSettings: _*)
  .settings(name := "scala-nameof")

lazy val nameofJVM = nameof.jvm
lazy val nameofJS = nameof.js

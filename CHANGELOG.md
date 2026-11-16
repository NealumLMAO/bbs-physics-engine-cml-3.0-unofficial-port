# CML 3.0 beta 9 update for Minecraft 1.21.1

This version updates the addon from BBS CML 3.0-beta-7-1.21.1 to
BBS CML 3.0-beta-9-1.21.1 using the supplied CML source tree and beta 9 jar.

## Changes

- Compile and launch against the beta 9 jar from libs/.
- Generate the exact CML manifest requirement from bbs_version so the packaged
  requirement stays aligned with the compile dependency.
- Match Fabric API 0.116.17+1.21.1 and the named Iris 1.8.8 / Sodium 0.6.13
  Minecraft 1.21.1 dependencies used by the supplied CML source build.
- Retain the Minecraft 1.21.1 shader supplier and baked item model APIs. The
  existing CML form editors, film tools, pose hooks, and structure integration
  compile and load against beta 9 without the 1.21.4 API changes.
- Extend the opt-in client smoke check with generated and block item bounds,
  display transforms, shaded/unshaded cloth and balloon draws, rope, picking
  shaders, and an OpenGL error check.
- Update compatibility documentation, retain the selected Gradle 9.8.0 wrapper
  distribution, and ignore both .og-sources/ and og-sources/ reference trees.

## Validation

- gradlew.bat build cmlSmokeClasses passed, including all six physics checks:
  cache, pose, settings, scene lifecycle, structure destruction, and simulation speed.
- gradlew.bat runClient -PcmlSmoke passed integration and rendering checks with
  Iris and Sodium installed and no active shader pack.
- A production client launched with the built physics jar and beta 9 CML jar from
  libs/; both addon entrypoints loaded and Jolt initialized successfully.
- The packaged manifest requires Minecraft 1.21.1 and BBS CML 3.0-beta-9-1.21.1.
  The separate smoke-check mod is excluded from the release jar.

Full film playback and active shader packs have not been manually validated.

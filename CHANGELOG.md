# Minecraft 1.21.4 port

This version targets Minecraft 1.21.4 and BBS CML 3.0-beta-9-1.21.4, continuing
from the CML 3.0-beta-7 port for Minecraft 1.21.1.

## Changes

- Update Minecraft, Yarn mappings, Fabric API, and the CML dependency to 1.21.4.
- Replace removed GameRenderer shader suppliers with 1.21.4 shader program keys
  and CML picking keys for collision wireframes, cloth, balloons, and rope.
- Measure item collision bounds through ItemRenderState, including layer display
  transforms, generated items, block items, and special item renderers.
- Use the inclusive world top height when collecting world collision blocks.
- Select the matching 1.21.4 Iris and Sodium development runtime dependencies.
- Generate the exact CML manifest requirement from bbs_version, keeping the
  packaged requirement aligned with the compile dependency.
- Extend the opt-in client smoke check with item geometry, display transforms,
  shaded and unshaded soft meshes, rope rendering, picking, and OpenGL error checks.
- Update compatibility and build documentation and retain the Gradle 9.8.0 wrapper
  used to build this version.
- Ignore both .og-sources/ and og-sources/, alongside local dependency jars and
  generated workspace files.

The existing CML integration for physics forms, film tools, collision editing,
ragdolls, chains, baking, and structure destruction remains the basis of this port.
The supplied CML source tree serves as the API reference; compilation and launch
still require the matching compiled CML jar in libs/.

## Validation

- gradlew.bat build cmlSmokeClasses passed, including all six physics checks:
  cache, pose, settings, scene lifecycle, structure destruction, and simulation speed.
- gradlew.bat runClient -PcmlSmoke passed the CML integration and rendering checks
  with Iris and Sodium installed and no active shader pack.
- A production client launched with the packaged physics jar and the CML jar from
  libs/; both addon entrypoints loaded and Jolt initialized successfully.
- The packaged manifest requires Minecraft 1.21.4 and BBS CML 3.0-beta-9-1.21.4;
  smoke-test code is excluded from the release jar.

Full film playback and active shader packs have not been manually validated.

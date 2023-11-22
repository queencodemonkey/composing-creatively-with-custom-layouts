# Composing Creatively with Custom Layouts

This project contains example code from my Droidcon NYC talk.
There are three launchers which can be independently launched
to show working examples.

Note: this code is for presentation/example purposes so the
more complex examples are not rigorously implemented but meant
to show the relatively power of custom canvas and layout work.

# Project Structure

This is a multi-module application project with:

1. An `app` module that contains the three example launches.
2. A `build-logic` module that contains convention plugins for various ecosystem and other Gradle plugins used. Convention plugin logic pulls heavily from https://github.com/android/nowinandroid but with several personal modifications.
3. Library code modules:
  - `imeji` contains custom Compose canvas drawing example that has a faux image cropper
  - `custom-modifier` contains a custom layout modifier by which custom logic can be reusably packaged as a modifier; this also helps to illustrate Compose Phases
  - `schedule` contains a simple custom Compose layout example and contrasts using platform containers vs. customizing layout via a faux scheduling component

# Launchers

To run the examples, create run configurations for the following activities:
1. app -> `rt.compose.custom.ImejiActivity`
1. app -> `rt.compose.custom.PolarCoordinatesActivity`
1. app -> `rt.compose.custom.ScheduleActivity`

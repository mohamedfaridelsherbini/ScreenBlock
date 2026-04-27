---
version: "1.0.0"
name: "ScreenBlock Design System"
description: "A zero-distraction design system optimized for deep work and AMOLED efficiency."
colors:
  primary: "#3F51B5" # IndigoPrimary
  primary-light: "#757DE8" # IndigoLight
  secondary: "#708090" # SlateSecondary
  accent: "#00E676" # VividGreen
  background-dark: "#000000" # PureBlack
  background-light: "#FFFFFF"
  surface-dark: "#121212"
  surface-light: "#F5F5F5"
typography:
  headline:
    fontFamily: "Sans-Serif"
    fontSize: "24sp"
    fontWeight: 700
  body:
    fontFamily: "Sans-Serif"
    fontSize: "16sp"
    fontWeight: 400
  timer:
    fontFamily: "Sans-Serif"
    fontSize: "48sp"
    fontWeight: 300
spacing:
  xs: "4dp"
  sm: "8dp"
  md: "16dp"
  lg: "24dp"
  xl: "32dp"
rounded:
  sm: "4dp"
  md: "8dp"
  lg: "12dp"
  full: "100%"
components:
  focus-container:
    backgroundColor: "{colors.background-dark}"
    padding: "{spacing.md}"
  timer-display:
    textColor: "#FFFFFF"
    typography: "{typography.timer}"
  action-button:
    backgroundColor: "{colors.primary}"
    textColor: "#FFFFFF"
    rounded: "{rounded.md}"
  preset-button:
    backgroundColor: "{colors.secondary}"
    textColor: "#FFFFFF"
    rounded: "{rounded.lg}"
---

## Overview
ScreenBlock is designed to eliminate cognitive load. The design system prioritizes high-contrast readability and energy efficiency on AMOLED displays. The primary metaphor is a "Digital Vault"—once entered, only permitted tools are accessible.

## Colors
The color palette is restricted to prevent overstimulation.
- **Dark Mode (Default)**: Uses a pure black background to blend with device bezels and reduce light emission. Indigo Light and Vivid Green provide functional status indicators.
- **Light Mode**: Provides a high-readability alternative for dashboard management and setup, using standard Material 3 surface tones.
- **Focus Screen Exception**: Regardless of the system theme, the Focus Screen remains in Pure Black to maintain its role as a distraction-free environment.

## Typography
Uses a clean, modern sans-serif scale.
- **Timer**: Large, thin weight for a sense of calm and clarity.
- **Headlines**: Bold Indigo tones for hierarchical importance.
- **Labels**: Slate grey to reduce visual noise for secondary information.

## Layout
Built on a strict 8dp baseline grid.
- **Safe Zones**: Generous padding (16dp-24dp) around interactive elements to prevent accidental exits.
- **Horizontal Lists**: Used for Allowed Apps and Emergency Contacts on the Focus Screen to keep the UI compact and organized.

## Shapes
Rounded corners follow a progressive scale.
- **Cards**: Large rounding (12dp) for a friendly, approachable feel in the dashboard.
- **Controls**: Medium rounding (8dp) for standard actions.
- **Status Badges**: Fully rounded for quick recognition.

## Components
- **Focus Container**: A full-screen overlay that consumes all system gestures.
- **Duration Presets**: Large touch targets (minimum 48dp height) for one-tap session starts.
- **Allowed App Icon**: Uniform sizing with clear text labels to ensure whitelisted tools are easy to identify.

## Do's and Don'ts
- **Do**: Use pure black (#000000) for all focus-related backgrounds.
- **Do**: Use Vivid Green only for successful session completions or "Allowed" states.
- **Don't**: Use vibrant animations or transitions during an active focus session.
- **Don't**: Use red for non-destructive actions; stick to Slate for neutral secondary UI.

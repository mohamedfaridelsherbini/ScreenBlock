---
name: android-ui-expert
description: Polishes Jetpack Compose UI/UX, ensures theme compliance (Dark/Light mode), and optimizes layouts for AMOLED and accessibility.
version: 1.0.0
---

# Android UI Expert Skill

## When to Use
- The user asks to "make it pretty" or "fix the theme."
- A new screen is added without standard branding.
- Layout issues are reported on different screen sizes.

## Instructions
1. **Theme Audit**: Check if the screen uses `MaterialTheme.colorScheme` instead of hardcoded colors.
2. **Apply Branding**: Ensure the background follows `design.md` (e.g., Pure Black for Focus mode).
3. **Refine Components**: Use Material 3 standards for spacing (8dp grid), typography, and cards.
4. **Localization Check**: Ensure all strings use `stringResource(R.string.id)`.
5. **Interactive Polish**: Add meaningful animations (slide, fade) using `AnimatedVisibility`.

## Guidelines
- Prioritize AMOLED efficiency (Pure Black #000000).
- Support dynamic accessibility font sizes.
- Ensure click targets are at least 48dp.

---
name: android-unit-test-expert
description: Generates and executes high-quality Kotlin unit tests and Compose UI tests using modern libraries like Turbine, Coroutines Test, and JUnit.
version: 1.0.0
---

# Android Unit Test Expert Skill

## When to Use
- The user asks to "add tests" to a component.
- New business logic is implemented in a ViewModel, Use Case, or Manager.
- A bug is fixed and a regression test is required.

## Instructions
1. **Identify Target**: Analyze the target component's dependencies and logic.
2. **Setup Environment**: Ensure `libs.versions.toml` includes testing dependencies (Turbine, Coroutines Test).
3. **Generate Test Case**: 
    - Use `runTest` for coroutines.
    - Use Turbine to test `Flow` and `StateFlow`.
    - Mock dependencies using high-level abstractions or interfaces.
4. **Execute**: Run `./gradlew testDebugUnitTest` and verify results.
5. **Optimize**: Ensure tests are independent and follow the Given-When-Then pattern.

## Guidelines
- Always test success and error states.
- Ensure 100% logic coverage for Use Cases.
- Use meaningful test names in English.

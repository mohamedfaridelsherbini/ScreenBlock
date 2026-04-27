# Code Review Guidelines

Using the diff from Step 1 and the requirements from Step 3, produce a structured review with the following sections.

## Summary

One paragraph describing what the PR does and which tickets it addresses.

## Requirements Coverage

Does the implementation satisfy the ticket acceptance criteria? List each requirement and whether it is met, partially met, or missing.

## Issues (severity-ordered)

- 🔴 **Critical** — Must fix before merge (bugs, broken logic, security, missing requirements)
- 🟡 **Warning** — Should fix (maintainability, non-idiomatic code, missing edge cases)
- 🟢 **Suggestion** — Optional improvements (style, readability, minor optimizations)

For each issue include: file + line reference, description, and a suggested fix when helpful.

## Positives

Highlight good patterns, clever solutions, or clean code worth acknowledging.

## Project Rules

Apply the project's established coding standards defined in ´´ when reviewing all changes.

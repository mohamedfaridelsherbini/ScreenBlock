---
name: pr-code-review
description: Performs a structured code review for a pull request. Use when the user provides a PR URL or asks to review changes against Jira tickets.
version: 1.0.0
---

# PR Code Review Skill

## When to Use
- The user shares a Bitbucket/GitHub PR URL.
- The user asks to review a set of changes before a commit.
- Verification of requirements against Jira tickets is needed.

## Instructions
1. **Fetch PR/Diff**: Extract metadata and the full code diff from the provided URL or local Git state.
2. **Sync Context**: Switch to the target branch and ensure a clean environment.
3. **Analyze Requirements**: Parse ticket IDs from titles and descriptions. Fetch full ticket details to understand the "Why" and "Acceptance Criteria."
4. **Execute Review**: Cross-reference the diff against the requirements and the project's design principles.
5. **Generate Output**: Produce a structured review following the `code-review-guidelines.md`.

## Guidelines
- Prioritize functional correctness and security (Critical).
- Check for idiomatic Kotlin and Compose usage (Warning).
- Ensure all hardcoded strings are moved to `strings.xml`.
- Verify theme support (Dark/Light mode).

## References
- [Code Review Guidelines](references/code-review-guidelines.md)
- [Android Coding Standards](https://developer.android.com/kotlin/style-guide)

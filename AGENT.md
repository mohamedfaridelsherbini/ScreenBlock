# ScreenBlock Agent Architecture

ScreenBlock is built using an "Agent-based" architecture where specialized controllers (Agents) handle business logic and low-level system integrations (Skills).

## Core Agents

### 1. FocusSessionManager
- **Role**: The primary orchestrator of the focus state.
- **Responsibilities**:
    - Managing the active `FocusSession` state.
    - Running the countdown timer.
    - Persistence of sessions to the Room database.
    - Exposing real-time stats (remaining time, blocked counts).

### 2. AppBlockingEngine
- **Role**: The decision-maker for distraction filtering.
- **Responsibilities**:
    - Maintaining a list of "Blocked" vs "Allowed" packages.
    - Detecting system launchers and assistants.
    - Evaluating whether a package should be blocked based on the current focus state and "Strict Mode" settings.

### 3. PermissionManager
- **Role**: The gatekeeper for system-level access.
- **Responsibilities**:
    - Checking and requesting Accessibility, Notification, and Battery permissions.
    - Interfacing with Android's `RoleManager` for automatic home launcher setup.

## Skills (System Services)

### 1. ScreenBlockAccessibilityService
- **Skill**: Window Observation.
- **Interaction**: Feeds foreground package names to the `AppBlockingEngine` and performs the `redirectToFocusScreen()` action if blocked.

### 2. ScreenBlockNotificationListener
- **Skill**: Notification Interception.
- **Interaction**: Monitors incoming notifications and cancels those from blocked apps during a session.

### 3. FocusForegroundService
- **Skill**: Persistence.
- **Interaction**: Keeps the application process alive and visible to the system during long focus sessions.

# ScreenBlock Design Specification

ScreenBlock's design philosophy is "Zero Distraction." The UI is optimized for AMOLED screens and cognitive focus.

## Visual Theme
- **Dark Mode (Default)**: Pure Black (`#000000`) background for AMOLED energy efficiency and minimal visual stimulation. Uses Indigo Light and Slate for accents.
- **Light Mode**: Clean White background with Indigo Primary and Slate elements. Provides high readability for setup and dashboard.
- **Focus Screen**: ALWAYS remains in Pure Black (AMOLED optimized) regardless of the system theme to ensure maximum cognitive focus and zero distraction.
- **Accent Color**: Vivid Green for critical status (Success, Allowed).
- **Typography**: Clean, sans-serif font (Material 3).

## Screen Breakdown

### 1. Onboarding & Setup
- **UX Goal**: Trust and Permission.
- **Elements**: Clear cards for each permission with status indicators (✅/❌). Simple explanation text for *why* each is needed.

### 2. Home Screen (Dashboard)
- **UX Goal**: Readiness and History.
- **Elements**:
    - **Progress Card**: High-contrast summary of today's focus time.
    - **Quick Actions**: 25, 50, and 90-minute presets.
    - **Settings**: One-tap access to Allowed Apps and Emergency Contacts.
    - **History**: Vertical list of past sessions with distraction badges.

### 3. Focus Screen (The "Locker")
- **UX Goal**: Extreme Minimization.
- **Elements**:
    - **Large Timer**: Central focus point showing MM:SS.
    - **Allowed Apps Row**: Horizontal shortcuts for whitelisted productivity tools.
    - **Emergency Row**: One-tap dial buttons for safety contacts.
    - **End Button**: Positioned at the bottom, styled distinctly to prevent accidental taps.

### 4. App Selection
- **UX Goal**: Customization.
- **Elements**: Searchable list of all installed apps with checkboxes.

## Key UX Mechanisms
- **Auto-Relaunch**: In Strict Mode, any attempt to navigate away is met with an instant slide-up animation of the Focus Screen.
- **Back Button Interception**: The back gesture is disabled during focus to prevent habitual "exit" gestures.

# HanapAral Project

HanapAral is a study group finder application designed to help students connect, collaborate, and manage study sessions.

---

## Project Structure

The project follows a modular Android architecture to separate concerns between data, UI, and business logic:

```text
HanapAral2/
├── app/
│   ├── build.gradle.kts
│   ├── google-services.json
│   ├── AndroidManifest.xml    
│   ├── proguard-rules.pro     
│   └── src/main/
│       ├── java/com/hanapAral/app/
│       │   ├── HanapAralApp.kt
│       │   ├── data/
│       │   │   ├── model/         (Data classes: User, StudyGroup, etc.)
│       │   │   ├── remote/        (API/Firebase Services)
│       │   │   └── repository/    (Data handling and logic)
│       │   ├── ui/
│       │   │   ├── auth/          (Authentication Activities)
│       │   │   ├── groups/        (Group Management UI)
│       │   │   ├── home/          (Main Application Hub)
│       │   │   ├── profile/       (User Profile Activities)
│       │   │   ├── admin/         (Moderation UI)
│       │   │   └── screens/       (Jetpack Compose UI components)
│       │   ├── viewmodel/         (State Management)
│       │   └── util/              (Helpers: Constants, Biometrics, Extensions)
│       └── res/
│           ├── drawable/          (Icons and Graphics)
│           ├── values/            (Themes, Colors, Strings)
│           └── xml/               (Network and Storage configurations)
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## Project Timeline & Contributions (Based on Git History)

### 📌 March 29 - April 1: Foundation & Core Features
*   **Pinca:** Established project infrastructure, navigation flow, and Firebase integration.
*   **Pelayo:** Initial UI designs for login and group listing.
*   **Pamintuan:** Implementation of authentication logic and Firestore data saving.
*   **Panganiban:** Database schema design for users and study groups.
*   **Pesito:** Lifecycle management and initial system testing.

### 📌 April 2: Cloud Messaging & FCM
*   **Pelayo:** Defined messaging models (`ChatMessage`, `Announcement`) and initiated `GroupDetailActivity`.
*   **Pamintuan:** Integrated Firebase Cloud Messaging (FCM) for push notifications.
*   **Panganiban (Paolo):** Implemented user registration logic in `ProfileSetupActivity` and FCM token storage.

### 📌 April 3: Remote Config & UI Expansion
*   **Pelayo (Dxtrply):** Committed major UI components: `GroupDetailScreen`, `ReminderScreen`, and `HomeScreen`.
*   **Pesito (Ronel):** Implemented `GroupsFragment`, `AdminViewModel`, and validated join group restrictions.
*   **Panganiban (Paolo):** Configured Remote Config parameters for feature toggles and group limits.

### 📌 April 4: Final Polishing & Debugging
*   **Pinca:** Finalized application permissions, security rules (ProGuard), and biometric helper. Integrated final assets and Google services.
*   **Panganiban (Paolo):** Resolved backend logic bugs in repositories.
*   **Pesito (Ronel):** Conducted final UI/UX polishing and transition refinements.

---

## Git Workflow Guidelines

To maintain a clean and stable codebase, the team follows these guidelines:

1.  **Branching Strategy:** Every member works on a dedicated branch named after their surname (e.g., `git checkout -b Pelayo`).
2.  **Commit Conventional Prefixes:**
    *   `feat:` for new features or structural additions.
    *   `fix:` for bug fixes.
    *   `style:` for UI/UX improvements or resource updates.
    *   `test:` for adding or performing tests.
    *   `chore:` for configuration and maintenance tasks.
3.  **Collaboration:** Development occurs in the `develop` branch via Pull Requests from member branches.
4.  **Final Integration:** Stable code is merged into the `main` branch for production readiness.

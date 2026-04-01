# HanapAral Project

HanapAral is a study group finder application designed to help students connect, collaborate, and manage study sessions. This document outlines the project structure, individual team contributions, and the development workflow.

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

## Team Contributions

### Pinca (Team Lead)
Responsible for core infrastructure, security, and project management.
*   **Infrastructure:** Handled build configurations (Gradle), Firebase project initialization, and ProGuard security rules.
*   **Core Logic:** Implemented the main navigation flow, authentication lifecycle, and dynamic feature toggles via Remote Config.
*   **Security & Utils:** Developed the biometric authentication helper and established global constants for the project.
*   **DevOps:** Managed the GitHub repository, performed code reviews, and handled branch merging from `develop` to `main`.

### Pelayo (Lead UI/UX Developer)
Responsible for the visual design and end-to-end user experience.
*   **UI Design:** Designed and implemented the major Jetpack Compose screens, including the Home, Login, Profile, and Group Creation screens.
*   **Interactive Components:** Built the UI for group announcements, chat bubbles, and study reminders.
*   **Quality Assurance:** Led the full system testing to ensure UI consistency and smooth transitions across all modules.

### Pamintuan (Authentication & User Logic)
Responsible for user onboarding and backend integration.
*   **Authentication:** Implemented Google Sign-In logic and handled the authentication repository.
*   **User Onboarding:** Developed the backend logic for user registration and profile setup activities.
*   **Cloud Services:** Integrated Firebase Cloud Messaging (FCM) for push notifications and set up the Remote Config infrastructure.
*   **Resources:** Managed the global `strings.xml` and network security configurations.

### Panganiban (Database & Admin Systems)
Responsible for data architecture and messaging systems.
*   **Data Modeling:** Defined the database structures for Users, Study Groups, and Chat Messages in Firestore.
*   **Backend Services:** Implemented token management for cloud messaging and handled member list updates in the database.
*   **Admin Tools:** Developed the Admin Panel dashboard and moderation tools.
*   **Configurations:** Managed the Android Manifest and XML file path configurations for storage.

### Pesito (Lifecycle & Testing)
Responsible for application stability and moderation state.
*   **App Lifecycle:** Initialized notification channels and managed the main Application class logic.
*   **Testing:** Conducted rigorous testing for login/logout flows, group joining restrictions, and notification delivery.
*   **State Management:** Developed the Admin and Profile ViewModels to manage moderation and user states.
*   **Validation:** Implemented input field validations and utility extensions to ensure data integrity.

---

## Git Workflow

To maintain a clean and stable codebase, the team follows these guidelines:

1.  **Branching Strategy:** Every member works on a dedicated branch named after their surname (e.g., `git checkout -b Pelayo`).
2.  **Incremental Progress:** Developers are encouraged to commit in small batches rather than uploading large blocks of code at once.
3.  **Commit Conventional Prefixes:**
    *   `feat:` for new features or structural additions.
    *   `fix:` for bug fixes.
    *   `style:` for UI/UX improvements or resource updates.
    *   `test:` for adding or performing tests.
    *   `chore:` for configuration and maintenance tasks.
4.  **Collaboration:** Once a task is complete, a **Pull Request (PR)** is created targeting the `develop` branch.
5.  **Final Integration:** After verification and debugging, the lead merges the `develop` branch into `main` for the final build.

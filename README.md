# Architecture components sample

master branch status

![master](https://github.com/zorin-egor/ArchitectureComponentsSample/actions/workflows/build-tests.yml/badge.svg)

<b>ArchitectureComponentsSample</b> - sample multi modals app developed with android architecture components, conventions plugins.

``` text
├── app.............. Entry point to the mobile application
│   └── NavHost.... App navigation coordination
├── core......... Independent project/component logic
│   ├── common.......... Utilities, extension functions, helpers
│   ├── network.......... Interaction with the network
│   ├── datastore.......... Logic for saving primitive data and objectsв
│   ├── datastore-proto.... Description of interaction models
│   ├── database.... Database
│   ├── data........ Repositories
│   ├── domain...... Business logic
│   ├── model....... Business logic models
│   ├── designsystem....... Basic UI components, themes, color schemes
│   ├── ui....... Comprehensive UI components for a specific presentation
├── features....... All screens are divided into module-features
│   ├── users.......... Feature list of users
│   ├── user_details.......... Feature details about the user
│   ├── repositories.......... Feature list of repositories
│   ├── repository_details.....Feature details about the repository
│   └── settings.......... Feature customization of the application theme
└──gradle-plugins.......... Convention gradle plugin for forwarding dependencies between modules
```

## Compose compiler metrics

Run the following command to get and analyse compose compiler metrics:

```bash
./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true
```

## Screenshots
<p align="center">
  <a>
    <img alt="ArchitectureComponentsSample" src="https://github.com/user-attachments/assets/40108436-ea58-405a-9a4a-a840db507053" />
  </a>
  <br/>
  <a>
    <img alt="ArchitectureComponentsSample" src="https://github.com/user-attachments/assets/2c40e579-618a-4169-9d45-46cf5601cea9" />
  </a>
</p>

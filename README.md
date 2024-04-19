# Architecture components sample

<b>ArchitectureComponentsSample</b> - sample multi modals app developed with android architecture components, conventions plugins.

``` text
├── app.............. Entry point to the mobile application
│   └── NavHost.... App navigation coordination
├── core......... Independent project/component logic
│   ├── common.......... Utilities, extension functions, helpers
│   ├── network.......... Interaction with the network
│   ├── datastore.......... Logic for saving primitive data and objectsв
│   ├── datastore-proto.......... Description of interaction models
│   ├── database....... Database
│   ├── data....... Repositories
│   ├── domain....... Business logic
│   ├── model....... Business logic models
│   ├── designsystem....... Basic UI components, themes, color schemes
│   ├── ui....... Comprehensive UI components for a specific presentation
├── features....... All screens are divided into module-features
│   ├── users.......... Feature list of users
│   ├── details.......... Feature details about the user
│   └── settings.......... Feature customization of the application theme
└──gradle-plugins.......... Convention gradle plugin for forwarding dependencies between modules
```

## Screenshots
<p align="center">
  <a>
    //<img alt="ArchitectureComponentsSample" src="https://github.com/zorin-egor/ArchitectureComponentsSample/assets/13707343/24a332fc-380c-4965-8261-0a741e19d239" />
  </a>
</p>

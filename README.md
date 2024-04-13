# Architecture components sample

<b>ArchitectureComponentsSample</b> - sample app developed with android architecture components.

``` text
├── app.............. Точка входа в мобильное приложение
│   ├── MainWindow.... Координирует всю навигацию в приложении
│   └── MainNavBar......... Нижнее навигационное меню
├── core......... Независимая логика проекта/компонентов
│   ├── datastore.......... Логика для сохранения мелких локальных файлов
│   ├── nav....... Логика для навигации
│   ├── data....... Бизнес логика
│   ├── domain....... Прослойка между UI и бизнес логикой для упрощения взаимодействия
│   ├── ui
│   │   ├── components.. Базовые компоненты для всего UI
│   │   ├── ui.. Логика взаимодействия с состояниями, абстракция для MVI
├── features....... Все экраны разбиты на модуль-фичи
│   ├── users.......... Фича список пользователей
│   ├── details.......... Фича подробно о пользователе
│   ├── settings.......... Фича настойка темы
└──gradle-plugins.......... Convention gradle плагин для проброса зависимостей между модулями
```

## Screenshots
<p align="center">
  <a>
    //<img alt="ArchitectureComponentsSample" src="https://user-images.githubusercontent.com/13707343/95138829-d91b2380-0773-11eb-951d-cbe263b97a9e.gif" />
  </a>
</p>

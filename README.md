# GlobeSurfer

## Desciption
GlobeSurfer is an app that helps you seearch for countries around the world using a [Rest Api](https://restcountries.com/v3.1/all) of countries and displays info about any selected country such as country's capital, flag, population, area, spoken languages, continent, currency, time zone and driving side.

## Features
- Search: You can search different countries using the search text box. The app implements editText onTextChangeListener to populate the search screen with countries with a name that matches the search text.
- Language: The API provides the name of the country in different languages. The App's Language button pupolates the screen with different countries with name matching the selected language. Even though the API translated the Country name in several languages, the app only includes translation in Englisgh, German, Chinese, Turkish, Spanish, Russia, Korean, Japanese and Arabic.
- Filter: The app let's you Filter the list of countries by continent and by time zones. You can select one or multiple filter preferences and tap on the Show results buttona and the app populates the screen with countries that matches the filter.

## Libraries Used
- JetPack libraries: Activity, Fragment, RecyclerView to display scrollable list, Navigation for app navigation, Hitl for dependency injection
- Material Design
- Retrofit for HTTP calls + ScalarsConverter
- Glide for Image processing and display

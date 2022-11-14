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
- Retrofit + ScalarsConverter to retrieve JSON fron API
- Glide for Image processing and display
- JSON-Java to parse and manipulate JSON

## Challenges Faced
Initially I was using Retrofit with the Moshi converter but some fields were returning null from the API. So I had to use the Scalar converter to return the raw JSON and use the JSON-Java Library

## Nice to have
I look forward to adding more translations for the country names in the app as the API provided and implementing Unit tests and also UI test. It would be nice to have the app display details, photos and may be videos of some cool tourist places in each country for those visiting the country just simplying planning a trip can know where to go without having to google it.

## Download
You can download the APK from [here](http://bit.ly/3Gvf055) or simply play the on Appetize [here](https://appetize.io/app/36xrdvurhmf36wscdtjj5quws4)

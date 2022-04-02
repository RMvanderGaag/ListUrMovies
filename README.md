# ListUrMovies
![Icon](https://i.imgur.com/aQSzvxm.png)
#### 📱 Android app for viewing and rating movies

## Features

- Search for movies
- Read detailed information about each movie
- Watch trailers
- Make lists of your favorite movies and save them for later
- Rate movies
- Filter on popularity, genre, rating etc.
- Multi-language
- Offline available
- Nightmode
- Share movies with your friends

## 🚀 Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Android studio
- Java JDK

### Installing
##### 1. Clone the repository by running this command
```
git clone https://github.com/RMvanderGaag/ListUrMovies.git
```

##### 2. Import the project in AndroidStudio, and add API Key
In Android Studio, go to File -> New -> Import project.
Follow the dialog wizard to choose the folder where you cloned the project and click on open.
Android Studio imports the projects and builds it for you.
Create a .xml file called secret inside the res/values folder and fill it as shown below.
Add TheMovieDatabase (TMDB) API Key inside the string

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="api_key">Api key here!</string>
</resources>
```

## Tech

- [Java] - Base language
- [Retrofit 2] - A type-safe HTTP client for Android and Java
- [Gson] - for serialization/deserialization Java Objects into JSON and back
- [Glide] - for Loading images
- [RoomDatabase] - Simplified SQLite use in Android
- [LiveData]
- [ViewModel]

## Development

ListUrMovies was made with [Android Studio] Bumblebee | 2021.1.1 Patch 2 and tested on an Android 12 device

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job, http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [git-repo-url]: <https://github.com/RMvanderGaag/ListUrMovies/>
   [Java]: <https://www.java.com/en>
   [Retrofit 2]: <https://square.github.io/retrofit/>
   [Gson]: <https://github.com/google/gson>
   [RoomDatabase]: <https://developer.android.com/training/data-storage/room>
   [Glide]: https://github.com/bumptech/glide
   [LiveData]: <https://developer.android.com/topic/libraries/architecture/livedata>
   [ViewModel]: https://developer.android.com/topic/libraries/architecture/viewmodel
   [Android Studio]: <https://developer.android.com/studio>

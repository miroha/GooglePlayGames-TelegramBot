### Google Play Games Telegram Bot

[![Telegram](https://img.shields.io/badge/telegram-chat-blueviolet)](https://t.me/google_play_games_bot)
[![Download](https://img.shields.io/badge/download-v1.0-blue)](https://github.com/miroha/GooglePlayGames-TelegramBot/releases/tag/1.0)
[![README_RU](https://img.shields.io/badge/readme-RU-brightgreen)](https://github.com/miroha/GooglePlayGames-TelegramBot/blob/master/README.ru.md)
#### Features
___
`Google Play Games Telegram Bot` provides the following features:

- Mode "Parser"
   - Supports only Google Play (and only [Games](https://play.google.com/store/apps/category/GAME)) URLs.
   - The following game information will be parsed:
      - Title;
      - Genre;
      - Overview;
      - Current version;
      - Date of last update;
      - Installation file size (only .apk without.obb part);
      - System Requirements (min Android OS);
      - Price (for paid games);
      - Availability of in-app purchases;
      - Developer name and contacts;
      - Recent changes (what's new in the last update);
      - Installs;
      - Average rating based on user reviews.
   - Received information will be sent to user who requested it. 
   Also, this information will be saved into the external database (if there is no such game yet) 
   to further use in the 'Library' mode.
   
- Mode "Library"
   - Provides information about the game by title (if exists in the external library) 
   via an interactive keyboard with ability to select certain info. 
   Also, there is a feature of following links directly to the page with the game in Google Play store.
   - Case-insensitive search.
   - If upon request several suitable games will be found, for example, 
   the user requests information about the game with title `Geometry Dash`, then bot can return similar titles in response, 
   for example, `Geometry Dash World` and `Geometry Dash Meltdown` and will ask to specify the title until one single match will be found.
   - Also, allows receiving information about the game by title in JSON.


#### Language interface

---
Now supports only Russian language.


#### Technology stack
___
- Java 11
- [Telegram Bot API](https://github.com/rubenlagus/TelegramBots)
- Spring Boot
- MongoDB (via Spring Data)
- [Project Lombok](https://projectlombok.org/)
- [Jsoup Parser](https://github.com/jhy/jsoup)
- Logback - logger
- Gradle - build tool

#### Powered by Intellij
___
<p align="center">
   <a href="https://www.jetbrains.com/"><img src="https://user-images.githubusercontent.com/14723332/87232472-0439e500-c3c8-11ea-8e21-f81ea3af8b70.png" width="100"></a>
</p>

#### Quick demonstration
___

<p align="center">
  <img src="https://user-images.githubusercontent.com/14723332/87232153-807ef900-c3c5-11ea-8fea-87cab00cfe46.png" width="800" height="450">
</p>

#### Try now
___
Talk to bot in Telegram: [@google_play_games_bot](https://t.me/google_play_games_bot)

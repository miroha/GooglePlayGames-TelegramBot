### Google Play Games Telegram Bot
[![Telegram](https://img.shields.io/badge/telegram-chat-blueviolet)](https://t.me/google_play_games_bot)
[![Download](https://img.shields.io/badge/download-v1.0-blue)](https://github.com/miroha/GooglePlayGames-TelegramBot/releases/tag/1.0)
[![README_RU](https://img.shields.io/badge/readme-EN-brightgreen)](https://github.com/miroha/GooglePlayGames-TelegramBot/blob/master/README.md)
#### Возможности бота
___
`Google Play Games Telegram Bot` обеспечивает следующую функциональность:

- Режим "Парсер"
   - Поддерживаются только Google Play (и только раздел [игр](https://play.google.com/store/apps/category/GAME)) ссылки.
   - Распарсена будет следующая информация об игре:
      - Название;
      - Жанр;
      - Текущая версия;
      - Дата последнего обновления;
      - Размер установочного файла (.apk, .obb не учитывается);
      - Системные требования (минимальная версия Android OS);
      - Цена;
      - Информация о наличии внутриигровых покупок;
      - Информация о разработчике;
      - Что нового в последнем обновлении;
      - Средний рейтинг на основе пользовательских отзывов.
   - Полученная информация от парсера будет отправлена пользователю,
   который её запрашивал, а также будет сохранена во внутреннюю библиотеку бота
   (если такой игры ещё там нет) для дальнейшего использования в режиме `"Библиотека"`. 
   
- Режим "Библиотека"
   - Предоставляет информацию об игре по её названию (если такая есть в библиотеке бота) в виде интерактивной клавиатуры
   с возможностью выбора интересующей информации, а также поддержкой перехода непосредственно
   на страницу с игрой в магазине Google Play.
   - Поиск по названию не чувствителен к регистру.
   - Если по запросу будет найдено несколько подходящих игр,
   например, пользователь запросил информацию об игре `Geometry Dash`, 
   то в ответ бот может вернуть несколько подходящих названий, например, `Geometry Dash World` и `Geometry Dash Meltdown` и попросит уточнить запрос
   до тех пор, пока не будет найдено одно единственное совпадение по названию.

#### Стек используемых технологий
___
- Java 11
- [Telegram Bot API](https://github.com/rubenlagus/TelegramBots)
- Spring Boot
- MongoDB (Spring Data)
- [Project Lombok](https://projectlombok.org/)
- [Jsoup](https://github.com/jhy/jsoup) для парсинга
- Logback для логирования
- Gradle для сборки

#### Создано с помощью Intellij
___
<p align="center">
   <a href="https://www.jetbrains.com/"><img src="https://user-images.githubusercontent.com/14723332/87232472-0439e500-c3c8-11ea-8e21-f81ea3af8b70.png" width="100"></a>
</p>

#### Демонстрация работы
___

<p align="center">
  <img src="https://user-images.githubusercontent.com/14723332/87232153-807ef900-c3c5-11ea-8fea-87cab00cfe46.png" width="800" height="450">
</p>

#### Протестировать бота
___
Бот доступен в Telegram: [@google_play_games_bot](https://t.me/google_play_games_bot)
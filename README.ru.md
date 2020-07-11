### Google Play Games Telegram Bot
![Java](https://img.shields.io/badge/java-11-yellow)
[![Telegram](https://img.shields.io/badge/telegram-chat-blueviolet)](https://t.me/google_play_games_bot)
[![Download](https://img.shields.io/badge/download-1.0-blue)](https://github.com/miroha/GooglePlayGames-TelegramBot/releases/tag/1.0)
[![README_RU](https://img.shields.io/badge/readme-EN-brightgreen)](https://github.com/miroha/GooglePlayGames-TelegramBot/blob/master/README.md)
#### Возможности бота
___
`Google Play Games Telegram Bot` обеспечивает следующую функциональность:

- Режим "Парсер"
   - Поддерживаются только Google Play (и только раздел [игр](https://play.google.com/store/apps/category/GAME)) ссылки.
   - Распарсена будет следующая информация об игре:
      - Название;
      - Категория;
      - Текущая версия;
      - Дата последнего обновления;
      - Размер установочного файла (.apk, .obb не учитывается);
      - Системные требования (минимальная версия Android OS);
      - Цена;
      - Информация о наличии внутриигровых покупках;
      - Почта для связи с разработчиком;
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
   например, пользователь запросил информацию об игре `Dead`, 
   то в ответ бот может вернуть несколько подходящих названий, например, `Dead Cells` и `Dead Trigger` и попросит уточнить запрос
   до тех пор, пока не будет найдено одно единственное совпадение по названию.

#### Стек используемых технологий
___
- Java 11
- [Telegram Bot API](https://github.com/rubenlagus/TelegramBots)
- Spring Boot
- MongoDB (Spring Data)
- [Project Lombok](https://projectlombok.org/)
- [Jsoup](https://github.com/jhy/jsoup)
- Logback для логирования
- Gradle для сборки

#### Протестировать бота
___
Бот доступен в Telegram: [@google_play_games_bot](https://t.me/google_play_games_bot)
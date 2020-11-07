## Currency Converter
Web-приложение построено с использованием Spring Boot. 
Приложение использует внешнюю базу данныx Postgres для хранения информации о зарегистрированных 
пользователях, данных о курсах валют и истории запросов.
"Общение" с DB Postgres реализовано посредством Hibernate.
Стек технологий, на котором реализовано приложение: SpringBoot, SpringSecurity, PostgreSQL, шаблонизатор Thymeleaf, библиотека Jackson.

## Запуск приложения

После запуска приложение может быть доступно по адресу:
[https://spring-currency-converter.herokuapp.com/](https://spring-currency-converter.herokuapp.com/)

[http://localhost:8189/index](http://localhost:8189/index) 
Авторизация в приложении возможна только при подключенной реляционной базы данных "postgresql".


## Детали Приложения

Для доступа к странице приложения "Конвертер валют" требуется авторизация.
Доступен гостевой доступ в приложение: Login: guest / Password: guest .
На главной странице приложения реализована демонстрация актуального курса валют.
Данные по курсам валют обновляемые. Курсы валют выгружаются с сайта 
[https://www.cbr-xml-daily.ru/daily_json.js](https://www.cbr-xml-daily.ru/daily_json.js) 
Значения из полученного JSON-файла преобразовываются в сущности и сохраняются в таблицах 
базы данных Postgres. При старте программы значения курсов автоматически обновляются.
Более точный расчет значения конвертации доступен после 11:30 утра. Это связано с 
обновлением данных по курсам валют Центральным банком 
РФ примерно в это время.

Реализовано:
1) Возможность авторизации посредством обращения к базе данных Postgres.
2) Обработка входящего url с json-данными по валютам, "парсинг" данных и их проекция на странице "index".
3) Конвертер валюты.
4) Сохранение истории, индивидуальной для каждого пользователя.
5) Регистрация пользователей.
6) Возможность просмотра курсов на заданную дату.
7) 


На стадии разработки:
1) Сохранение результатов конвертации в PDF-файл.
2) Проработать логику вывода данных по валютам, в зависимости от дней недели
3) Настроить регистрацию и активацию пользователя по отправке валидационного ключа на почту.

Исправлено в версии:
1) Убрал сущность Role. Заменил ее на Enum
2) Добавил Lombok
3) Оптимизировал метод loadUserByUsername в классе UserServiceImpl implements UserService, UserDetailsService
4) Добавил новый слой, предназначенный для работы с фронтом- DTO
5) Хранение данных по текущим курсам теперь организовано в коллекции MAP. Это даст возможность сохранять данные по курсам 
валют на каждый день и обращаться к ним по случаю необходимости.
6) Изменена настройка spring.jpa.hibernate.ddl-auto = validate. Данная настройка сверяет данные, посылаемые в DB,
 и в случае их отсутствия добавляет новые записи в таблицы.
7) Добавлена защита формы регистрации, запрещающая отправлять пустые значения с формы.
8) На фронте в методах теперь используются сущности dto. Все сущности dao убраны из логики работы методов контроллеров.
9) Убрал класс ViewCurrencies. Его функционал передал в dto в ValuteDto.
10) Добавил страницу для пользователей, не завершивших регистрацию.
11) Отсортировал выводимые данные по названию валют в алфавитном порядке.
12) Валидация регистрационной формы: требуется заполнить все формы для регистрации пользователя.
13) Привел в порядок методы по работе с фронтом. Теперь в логике работы используются dto вместо сущностей dao.
14) Настроено автоматическое обновление курсов валют, по указанному интервалу времени.
15) Обновлена страница "index".
16) Добавлена обработка возможного exception при запросе курсов валют на заданную дату.

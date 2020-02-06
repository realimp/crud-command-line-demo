# crud-command-line-demo
 Demo project for Spring Boot

### Задание
Создать консольное CRUD приложение. Ввод / вывод данных производиться через консоль. 

Стэк spring boot(spring), hibernate, h2 (MySQL и др.).  

В консоль вводится имя одной из create/update/delete/read операций и необходимые входные данные. 

Пример: read username, remove username. Каждая сущность должна содержать не менее 3 полей, и должна быть связана хотя бы с одной другой сущностью. 

Для проверки работоспособности приложения необходимо написать junit тесты.


#### Рекомендации для сборки и запуска проекта:
 - JDK 11
 - MySQL database server
 - Заменить данные для подключения к MySQL серверу в файле application.properties
 - Создание исполняемого jar командой  *mvn package spring-boot:repackage*

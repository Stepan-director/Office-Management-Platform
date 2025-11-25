Office Management Platform
Создать единую платформу для управления офисным пространством с системой бронирования рабочих мест, аутентификацией пользователей и внутренней коммуникацией.

Микроcервисная архитектура:

Auth Service:
- Регистрация пользователей (админом - ADMIN)
- Логин
- JWT генерация
- Управление ролями (ADMIN, DEPARTMENT_HEAD, PROJECT_MANAGER, SENIOR_DEVELOPER, DEVELOPER, JUNIOR_DEVELOPER)
- Refresh tokens

User Service:
Профили пользователей (email, phone, position, created)
- Организационная структура (позиция специалиста)
- Поиск сотрудников

Workspace Service(каталог рабочих мест)
- Создание/удаление рабочих мест (только ADMIN)
- Характеристики мест (этаж, номер места)
- Статус доступности

Booking Service:
- Бронирование рабочих мест
- Отмена бронирований

Базы данных - раздельные PostgreSQL для каждого сервиса

Технологии:
Java 17+;
Spring Boot 3.x;
Spring Security - аутентификация и авторизация(базовый уровень);
JWT (jjwt) - токены для stateless аутентификации(базовый уровень);
Spring Data JPA;
PostgreSQL;
Apache Kafka - асинхронная коммуникация между сервисами;
Docker - для настройки Kafka, ZooKeeper;
Postman - тестирование.

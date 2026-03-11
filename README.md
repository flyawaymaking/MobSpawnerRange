# MobSpawnerRange Plugin

Плагин для серверов Minecraft на основе Paper, который позволяет увеличить дистанцию активации спавна мобов из спавнеров.

## 📋 Описание

Стандартная дистанция активации спавнеров в Minecraft - 16 блоков. Этот плагин позволяет увеличить эту дистанцию, что особенно полезно для создания эффективных фарм-ферм и моб-арен.

## ⚡ Функциональность

- Увеличение радиуса активации спавнеров мобов
- Автоматическое применение настроек к новым спавнерам
- Команды для управления настройками в реальном времени
- Поддержка перезагрузки конфигурации без перезапуска сервера

## ⚙️ Конфигурация

```yaml
# MobSpawnerRange Configuration

# Базовая дистанция активации спавнеров в Minecraft - 16 блоков
activation-range: 32
debug-mode: false

messages:
  # Общие сообщения
  no-permission: "<red>У вас нет прав на эту команду!"
  invalid-number: "<red>Пожалуйста, введите корректное число!"

  # Команда reload
  reload-success: "<green>Конфигурация MobSpawnerRange перезагружена!"

  # Команда info
  info: |
    <gold>=== MobSpawnerRange ===
    <yellow>Текущая дистанция активации: <green>{range}</green> блоков
  info-debug-enabled: "<yellow>Режим отладки: <green>включен"
  info-debug-disabled: "<yellow>Режим отладки: <red>выключен"

  # Команда setrange
  setrange-usage: "<red>Использование: /spawnerrange setrange <дистанция>"
  setrange-invalid: "<red>Дистанция должна быть числом!"
  setrange-success: "<green>Дистанция активации установлена на {range} блоков!"
  setrange-updated: "<green>Все спавнеры обновлены!"

  # Команда updateall
  updateall-success: "<green>Все спавнеры обновлены с текущими настройками!"

  # Команда help
  help-header: "<gold>=== MobSpawnerRange Помощь ==="
  help-info: "<yellow>/spawnerrange info <white>- Информация о плагине"
  help-reload: "<yellow>/spawnerrange reload <white>- Перезагрузить конфиг"
  help-setrange: "<yellow>/spawnerrange setrange <дистанция> <white>- Установить дистанцию"
  help-updateall: "<yellow>/spawnerrange updateall <white>- Обновить все спавнеры"

  # Ошибки
  unknown-command: "<red>Неизвестная команда. Используйте /spawnerrange help"
```

## 📝 Команды

- `/spawnerrange info` - информация о текущих настройках
- `/spawnerrange reload` - перезагрузить конфигурацию
- `/spawnerrange setrange <дистанция>` - установить новый радиус активации (применяется сразу)
- `/spawnerrange updateall` - обновить радиус активации активных спавнеров в соответствии с конфигом

## 🔐 Права доступа

- `mobspawnerrange.info` - просмотр информации (по умолчанию: op)
- `mobspawnerrange.reload` - перезагрузка конфигурации (по умолчанию: op)
- `mobspawnerrange.setrange` - изменение дистанции (по умолчанию: op)
- `mobspawnerrange.updateall` - все административные права (по умолчанию: op)

## 🎯 Использование

После установки плагина все новые спавнеры будут автоматически использовать увеличенный радиус активации. Для применения настроек к уже существующим спавнерам используйте команду `/spawnerrange updateall`.

## 🐛 Решение проблем

Если спавнеры не активируются на увеличенной дистанции:
1. Проверьте, что в конфигурации установлено значение больше 16
2. Используйте команду `/spawnerrange updateall` для применения настроек
3. Включите `debug-mode: true` для получения подробной информации в консоли

## 📦 Совместимость

- Paper 1.20+ (кроме 1.20.5)
- Java 21

## Установка

1. Скачайте **последний релиз** из раздела [Releases](../../releases)
2. Поместите его в папку `/plugins`
3. Перезапустите сервер
4. Настройте config.yml при необходимости
5. Используйте `/mobspawner reload`, чтобы применить изменения без перезапуска
6. Если в конфиге изменили `activation-range`, выполните команду `/mobspawner updateall`

---

## 📄 Лицензия

Плагин распространяется под лицензией MIT.

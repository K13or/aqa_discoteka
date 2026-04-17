# README

## Локальный запуск тестов

### API-тесты

1. Создайте и активируйте виртуальное окружение.
2. Установите Python-зависимости:

```bash
pip install -r requirements.txt
```

3. Убедитесь, что в файле `.env` задан `BASE_URL`.
4. Запустите API-тесты:

```bash
pytest tests/api/test_dynamic_api.py
```

Чтобы запустить все Python-тесты, используйте:

```bash
pytest
```

### UI-тесты

1. Установите Node.js-зависимости:

```bash
npm install
```

2. Если браузеры Playwright ещё не установлены, выполните:

```bash
npx playwright install
```

3. Запустите UI smoke-тест:

```bash
npm run test:ui
```

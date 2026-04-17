# aqa_discoteka

Проект для автоматизированного тестирования API и UI.

## Описание

Репозиторий содержит заготовку AQA-пайплайна:
- UI-тесты на Playwright;
- API-тесты на Python/Pytest;
- утилиты для генерации тест-кейсов.

## Быстрый старт

```bash
npm install
npm run test:ui
```

## Локальный запуск тестов

### UI-тесты (Playwright)

```bash
npm install
npx playwright install
npm run test:ui
```

### API-тесты (Pytest)

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
pytest tests/api -v
```

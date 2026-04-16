import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: './tests/ui',
  timeout: 30_000,
  reporter: [['html', { open: 'never' }]],
  use: {
    headless: true,
    trace: 'retain-on-failure'
  }
});
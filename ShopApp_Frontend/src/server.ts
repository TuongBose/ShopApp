import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';
import express from 'express';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

// Mock localStorage on server
if (typeof window === 'undefined') {
  global['localStorage'] = {
    getItem: (key: string) => null,
    setItem: (key: string, value: string) => { },
    removeItem: (key: string) => { },
    clear: () => { },
    length: 0,
    key: (index: number) => null
  } as Storage;
}

if (typeof window === 'undefined') {
  const mockStorage = {
    // Khai báo _data như một thuộc tính riêng
    _data: new Map<string, string>(),

    getItem(key: string): string | null {
      return this['_data'].get(key) || null;
    },

    setItem(key: string, value: string): void {
      this['_data'].set(key, value);
    },

    removeItem(key: string): void {
      this['_data'].delete(key);
    },

    clear(): void {
      this['_data'].clear();
    },

    get length(): number {
      return this['_data'].size;
    },

    key(index: number): string | null {
      const keys = Array.from(this['_data'].keys()) as string[]; // keys là string[]
      if (index >= 0 && index < keys.length) {
        return keys[index]; // Trả về string khi index hợp lệ
      }
      return null; // Trả về null khi index ngoài phạm vi
    }
  } as Storage;

  // Gán mock cho cả localStorage và sessionStorage
  global['localStorage'] = { ...mockStorage };
  global['sessionStorage'] = { ...mockStorage };
}
const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');

const app = express();
const angularApp = new AngularNodeAppEngine();

/**
 * Example Express Rest API endpoints can be defined here.
 * Uncomment and define endpoints as necessary.
 *
 * Example:
 * ```ts
 * app.get('/api/**', (req, res) => {
 *   // Handle API request
 * });
 * ```
 */

/**
 * Serve static files from /browser
 */
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

/**
 * Handle all other requests by rendering the Angular application.
 */
app.use('/**', (req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next(),
    )
    .catch(next);
});

/**
 * Start the server if this module is the main entry point.
 * The server listens on the port defined by the `PORT` environment variable, or defaults to 4000.
 */
if (isMainModule(import.meta.url)) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

/**
 * Request handler used by the Angular CLI (for dev-server and during build) or Firebase Cloud Functions.
 */
export const reqHandler = createNodeRequestHandler(app);

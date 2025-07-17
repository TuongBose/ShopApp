import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

/*
npm install bootstrap @ng-bootstrap/ng-bootstrap
npm install font-awesome @fortawesome/fontawesome-free
npm install class-transformer class-validator
npm install @popperjs/core
npm install @auth0/angular-jwt
*/
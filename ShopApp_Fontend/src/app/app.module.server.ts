import { NgModule } from '@angular/core';
import { ServerModule } from '@angular/platform-server';
import { provideServerRouting } from '@angular/ssr';
import { AppModule } from './app.module';
import { serverRoutes } from './app.routes.server';
import { HomeComponent } from './components/home/home.component';
import { OrderComponent } from './components/order/order.component';
import { OrderConfirmComponent } from './components/order-confirm/order-confirm.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { provideHttpClient, withFetch } from '@angular/common/http';

@NgModule({
  imports: [AppModule, ServerModule],
  providers: [
    provideServerRouting(serverRoutes),
    provideHttpClient(withFetch())
  ],
  bootstrap: [DetailProductComponent],
})
export class AppServerModule {}

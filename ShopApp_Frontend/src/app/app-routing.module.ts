import { NgModule } from "@angular/core";
import { DetailProductComponent } from "./components/detail-product/detail-product.component";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { OrderConfirmComponent } from "./components/order-confirm/order-confirm.component";
import { OrderComponent } from "./components/order/order.component";
import { RegisterComponent } from "./components/register/register.component";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuardFn } from "./guards/auth.guard";

const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'products/:id', component: DetailProductComponent },
    { path: 'orders', component: OrderComponent, canActivate: [AuthGuardFn] },
    { path: 'user-profile', component: OrderComponent, canActivate: [AuthGuardFn] },
    { path: 'orders/:id', component: OrderConfirmComponent },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }

import { DetailProductComponent } from "./components/detail-product/detail-product.component";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { OrderConfirmComponent } from "./components/order-confirm/order-confirm.component";
import { OrderComponent } from "./components/order/order.component";
import { RegisterComponent } from "./components/register/register.component";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuardFn } from "./guards/auth.guard";
import { UserProfileComponent } from "./components/user-profile/user-profile.component";
import { AuthCallbackComponent } from "./components/auth-callback/auth-callback.component";
import { PaymentCallbackComponent } from "./components/payment-callback/payment-callback.component";

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'auth/google/callback', component: AuthCallbackComponent },
    { path: 'auth/facebook/callback', component: AuthCallbackComponent },
    { path: 'payments/payment-callback', component: PaymentCallbackComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'products/:id', component: DetailProductComponent },
    { path: 'orders', component: OrderComponent, canActivate: [AuthGuardFn] },
    { path: 'user-profile', component: UserProfileComponent, canActivate: [AuthGuardFn] },
    { path: 'orders/:id', component: OrderConfirmComponent },
];

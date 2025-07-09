import { NgModule } from "@angular/core";
import { LoginComponent } from "./components/login/login.component";
import { RouterModule, Routes } from "@angular/router";
import { AdminComponent } from "./components/admin/admin.component";
import { AdminGuardFn } from "./guards/admin.guard";
import { OrderComponent } from "./components/order/order.component";

const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'admin', component: AdminComponent, canActivate:[AdminGuardFn] },
    { path: 'orders', component: OrderComponent, canActivate:[AdminGuardFn] },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
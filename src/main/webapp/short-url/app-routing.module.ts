import { Routes } from '@angular/router';
// @ts-ignore
import { ShortUrlComponent } from './short-url/short-url.component';
// @ts-ignore
import { PageNotFoundComponent } from './layouts/page-not-found/page-not-found.component';
// @ts-ignore
import { HomeComponent } from './home/home.component';
// @ts-ignore
import { LoginComponent } from './account/login/login.component';
// @ts-ignore
import { RegisterComponent } from './account/register/register.component';
// @ts-ignore
import { AuthGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'shortener', component: ShortUrlComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', component: PageNotFoundComponent },
];

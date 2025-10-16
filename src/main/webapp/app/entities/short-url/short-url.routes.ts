import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ShortUrlResolve from './route/short-url-routing-resolve.service';

const shortUrlRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/short-url.component').then(m => m.ShortUrlComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/short-url-detail.component').then(m => m.ShortUrlDetailComponent),
    resolve: {
      shortUrl: ShortUrlResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/short-url-update.component').then(m => m.ShortUrlUpdateComponent),
    resolve: {
      shortUrl: ShortUrlResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/short-url-update.component').then(m => m.ShortUrlUpdateComponent),
    resolve: {
      shortUrl: ShortUrlResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default shortUrlRoute;

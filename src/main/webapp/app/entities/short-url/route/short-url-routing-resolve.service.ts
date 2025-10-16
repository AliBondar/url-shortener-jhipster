import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShortUrl } from '../short-url.model';
import { ShortUrlService } from '../service/short-url.service';

const shortUrlResolve = (route: ActivatedRouteSnapshot): Observable<null | IShortUrl> => {
  const id = route.params.id;
  if (id) {
    return inject(ShortUrlService)
      .find(id)
      .pipe(
        mergeMap((shortUrl: HttpResponse<IShortUrl>) => {
          if (shortUrl.body) {
            return of(shortUrl.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default shortUrlResolve;

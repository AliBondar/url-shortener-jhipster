import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShortUrl, NewShortUrl } from '../short-url.model';

export type PartialUpdateShortUrl = Partial<IShortUrl> & Pick<IShortUrl, 'id'>;

type RestOf<T extends IShortUrl | NewShortUrl> = Omit<T, 'createdAt' | 'expiryAt'> & {
  createdAt?: string | null;
  expiryAt?: string | null;
};

export type RestShortUrl = RestOf<IShortUrl>;

export type NewRestShortUrl = RestOf<NewShortUrl>;

export type PartialUpdateRestShortUrl = RestOf<PartialUpdateShortUrl>;

export type EntityResponseType = HttpResponse<IShortUrl>;
export type EntityArrayResponseType = HttpResponse<IShortUrl[]>;

@Injectable({ providedIn: 'root' })
export class ShortUrlService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/short-urls');

  create(shortUrl: NewShortUrl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shortUrl);
    return this.http
      .post<RestShortUrl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(shortUrl: IShortUrl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shortUrl);
    return this.http
      .put<RestShortUrl>(`${this.resourceUrl}/${this.getShortUrlIdentifier(shortUrl)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(shortUrl: PartialUpdateShortUrl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shortUrl);
    return this.http
      .patch<RestShortUrl>(`${this.resourceUrl}/${this.getShortUrlIdentifier(shortUrl)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestShortUrl>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestShortUrl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getShortUrlIdentifier(shortUrl: Pick<IShortUrl, 'id'>): number {
    return shortUrl.id;
  }

  compareShortUrl(o1: Pick<IShortUrl, 'id'> | null, o2: Pick<IShortUrl, 'id'> | null): boolean {
    return o1 && o2 ? this.getShortUrlIdentifier(o1) === this.getShortUrlIdentifier(o2) : o1 === o2;
  }

  addShortUrlToCollectionIfMissing<Type extends Pick<IShortUrl, 'id'>>(
    shortUrlCollection: Type[],
    ...shortUrlsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shortUrls: Type[] = shortUrlsToCheck.filter(isPresent);
    if (shortUrls.length > 0) {
      const shortUrlCollectionIdentifiers = shortUrlCollection.map(shortUrlItem => this.getShortUrlIdentifier(shortUrlItem));
      const shortUrlsToAdd = shortUrls.filter(shortUrlItem => {
        const shortUrlIdentifier = this.getShortUrlIdentifier(shortUrlItem);
        if (shortUrlCollectionIdentifiers.includes(shortUrlIdentifier)) {
          return false;
        }
        shortUrlCollectionIdentifiers.push(shortUrlIdentifier);
        return true;
      });
      return [...shortUrlsToAdd, ...shortUrlCollection];
    }
    return shortUrlCollection;
  }

  protected convertDateFromClient<T extends IShortUrl | NewShortUrl | PartialUpdateShortUrl>(shortUrl: T): RestOf<T> {
    return {
      ...shortUrl,
      createdAt: shortUrl.createdAt?.format(DATE_FORMAT) ?? null,
      expiryAt: shortUrl.expiryAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restShortUrl: RestShortUrl): IShortUrl {
    return {
      ...restShortUrl,
      createdAt: restShortUrl.createdAt ? dayjs(restShortUrl.createdAt) : undefined,
      expiryAt: restShortUrl.expiryAt ? dayjs(restShortUrl.expiryAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestShortUrl>): HttpResponse<IShortUrl> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestShortUrl[]>): HttpResponse<IShortUrl[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

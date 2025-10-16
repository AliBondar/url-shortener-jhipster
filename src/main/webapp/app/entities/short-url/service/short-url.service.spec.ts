import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IShortUrl } from '../short-url.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../short-url.test-samples';

import { RestShortUrl, ShortUrlService } from './short-url.service';

const requireRestSample: RestShortUrl = {
  ...sampleWithRequiredData,
  createdAt: sampleWithRequiredData.createdAt?.format(DATE_FORMAT),
  expiryAt: sampleWithRequiredData.expiryAt?.format(DATE_FORMAT),
};

describe('ShortUrl Service', () => {
  let service: ShortUrlService;
  let httpMock: HttpTestingController;
  let expectedResult: IShortUrl | IShortUrl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShortUrlService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ShortUrl', () => {
      const shortUrl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shortUrl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShortUrl', () => {
      const shortUrl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shortUrl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShortUrl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShortUrl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShortUrl', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addShortUrlToCollectionIfMissing', () => {
      it('should add a ShortUrl to an empty array', () => {
        const shortUrl: IShortUrl = sampleWithRequiredData;
        expectedResult = service.addShortUrlToCollectionIfMissing([], shortUrl);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shortUrl);
      });

      it('should not add a ShortUrl to an array that contains it', () => {
        const shortUrl: IShortUrl = sampleWithRequiredData;
        const shortUrlCollection: IShortUrl[] = [
          {
            ...shortUrl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShortUrlToCollectionIfMissing(shortUrlCollection, shortUrl);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShortUrl to an array that doesn't contain it", () => {
        const shortUrl: IShortUrl = sampleWithRequiredData;
        const shortUrlCollection: IShortUrl[] = [sampleWithPartialData];
        expectedResult = service.addShortUrlToCollectionIfMissing(shortUrlCollection, shortUrl);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shortUrl);
      });

      it('should add only unique ShortUrl to an array', () => {
        const shortUrlArray: IShortUrl[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shortUrlCollection: IShortUrl[] = [sampleWithRequiredData];
        expectedResult = service.addShortUrlToCollectionIfMissing(shortUrlCollection, ...shortUrlArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shortUrl: IShortUrl = sampleWithRequiredData;
        const shortUrl2: IShortUrl = sampleWithPartialData;
        expectedResult = service.addShortUrlToCollectionIfMissing([], shortUrl, shortUrl2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shortUrl);
        expect(expectedResult).toContain(shortUrl2);
      });

      it('should accept null and undefined values', () => {
        const shortUrl: IShortUrl = sampleWithRequiredData;
        expectedResult = service.addShortUrlToCollectionIfMissing([], null, shortUrl, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shortUrl);
      });

      it('should return initial array if no ShortUrl is added', () => {
        const shortUrlCollection: IShortUrl[] = [sampleWithRequiredData];
        expectedResult = service.addShortUrlToCollectionIfMissing(shortUrlCollection, undefined, null);
        expect(expectedResult).toEqual(shortUrlCollection);
      });
    });

    describe('compareShortUrl', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShortUrl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 32116 };
        const entity2 = null;

        const compareResult1 = service.compareShortUrl(entity1, entity2);
        const compareResult2 = service.compareShortUrl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 32116 };
        const entity2 = { id: 17835 };

        const compareResult1 = service.compareShortUrl(entity1, entity2);
        const compareResult2 = service.compareShortUrl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 32116 };
        const entity2 = { id: 32116 };

        const compareResult1 = service.compareShortUrl(entity1, entity2);
        const compareResult2 = service.compareShortUrl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

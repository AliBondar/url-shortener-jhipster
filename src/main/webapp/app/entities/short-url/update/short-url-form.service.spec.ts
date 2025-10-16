import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../short-url.test-samples';

import { ShortUrlFormService } from './short-url-form.service';

describe('ShortUrl Form Service', () => {
  let service: ShortUrlFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShortUrlFormService);
  });

  describe('Service methods', () => {
    describe('createShortUrlFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShortUrlFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            originalUrl: expect.any(Object),
            shortCode: expect.any(Object),
            createdAt: expect.any(Object),
            expiryAt: expect.any(Object),
            active: expect.any(Object),
            accessCount: expect.any(Object),
          }),
        );
      });

      it('passing IShortUrl should create a new form with FormGroup', () => {
        const formGroup = service.createShortUrlFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            originalUrl: expect.any(Object),
            shortCode: expect.any(Object),
            createdAt: expect.any(Object),
            expiryAt: expect.any(Object),
            active: expect.any(Object),
            accessCount: expect.any(Object),
          }),
        );
      });
    });

    describe('getShortUrl', () => {
      it('should return NewShortUrl for default ShortUrl initial value', () => {
        const formGroup = service.createShortUrlFormGroup(sampleWithNewData);

        const shortUrl = service.getShortUrl(formGroup) as any;

        expect(shortUrl).toMatchObject(sampleWithNewData);
      });

      it('should return NewShortUrl for empty ShortUrl initial value', () => {
        const formGroup = service.createShortUrlFormGroup();

        const shortUrl = service.getShortUrl(formGroup) as any;

        expect(shortUrl).toMatchObject({});
      });

      it('should return IShortUrl', () => {
        const formGroup = service.createShortUrlFormGroup(sampleWithRequiredData);

        const shortUrl = service.getShortUrl(formGroup) as any;

        expect(shortUrl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShortUrl should not enable id FormControl', () => {
        const formGroup = service.createShortUrlFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShortUrl should disable id FormControl', () => {
        const formGroup = service.createShortUrlFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

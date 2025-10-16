import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ShortUrlService } from '../service/short-url.service';
import { IShortUrl } from '../short-url.model';
import { ShortUrlFormService } from './short-url-form.service';

import { ShortUrlUpdateComponent } from './short-url-update.component';

describe('ShortUrl Management Update Component', () => {
  let comp: ShortUrlUpdateComponent;
  let fixture: ComponentFixture<ShortUrlUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shortUrlFormService: ShortUrlFormService;
  let shortUrlService: ShortUrlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ShortUrlUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ShortUrlUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShortUrlUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shortUrlFormService = TestBed.inject(ShortUrlFormService);
    shortUrlService = TestBed.inject(ShortUrlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const shortUrl: IShortUrl = { id: 17835 };

      activatedRoute.data = of({ shortUrl });
      comp.ngOnInit();

      expect(comp.shortUrl).toEqual(shortUrl);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShortUrl>>();
      const shortUrl = { id: 32116 };
      jest.spyOn(shortUrlFormService, 'getShortUrl').mockReturnValue(shortUrl);
      jest.spyOn(shortUrlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shortUrl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shortUrl }));
      saveSubject.complete();

      // THEN
      expect(shortUrlFormService.getShortUrl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shortUrlService.update).toHaveBeenCalledWith(expect.objectContaining(shortUrl));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShortUrl>>();
      const shortUrl = { id: 32116 };
      jest.spyOn(shortUrlFormService, 'getShortUrl').mockReturnValue({ id: null });
      jest.spyOn(shortUrlService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shortUrl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shortUrl }));
      saveSubject.complete();

      // THEN
      expect(shortUrlFormService.getShortUrl).toHaveBeenCalled();
      expect(shortUrlService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShortUrl>>();
      const shortUrl = { id: 32116 };
      jest.spyOn(shortUrlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shortUrl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shortUrlService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

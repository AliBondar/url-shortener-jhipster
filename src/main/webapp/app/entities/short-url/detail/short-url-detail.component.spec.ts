import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ShortUrlDetailComponent } from './short-url-detail.component';

describe('ShortUrl Management Detail Component', () => {
  let comp: ShortUrlDetailComponent;
  let fixture: ComponentFixture<ShortUrlDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShortUrlDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./short-url-detail.component').then(m => m.ShortUrlDetailComponent),
              resolve: { shortUrl: () => of({ id: 32116 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ShortUrlDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShortUrlDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load shortUrl on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShortUrlDetailComponent);

      // THEN
      expect(instance.shortUrl()).toEqual(expect.objectContaining({ id: 32116 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});

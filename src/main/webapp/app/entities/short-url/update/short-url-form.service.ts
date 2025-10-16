import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShortUrl, NewShortUrl } from '../short-url.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShortUrl for edit and NewShortUrlFormGroupInput for create.
 */
type ShortUrlFormGroupInput = IShortUrl | PartialWithRequiredKeyOf<NewShortUrl>;

type ShortUrlFormDefaults = Pick<NewShortUrl, 'id' | 'active'>;

type ShortUrlFormGroupContent = {
  id: FormControl<IShortUrl['id'] | NewShortUrl['id']>;
  originalUrl: FormControl<IShortUrl['originalUrl']>;
  shortCode: FormControl<IShortUrl['shortCode']>;
  createdAt: FormControl<IShortUrl['createdAt']>;
  expiryAt: FormControl<IShortUrl['expiryAt']>;
  active: FormControl<IShortUrl['active']>;
  accessCount: FormControl<IShortUrl['accessCount']>;
};

export type ShortUrlFormGroup = FormGroup<ShortUrlFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ShortUrlFormService {
  createShortUrlFormGroup(shortUrl: ShortUrlFormGroupInput = { id: null }): ShortUrlFormGroup {
    const shortUrlRawValue = {
      ...this.getFormDefaults(),
      ...shortUrl,
    };
    return new FormGroup<ShortUrlFormGroupContent>({
      id: new FormControl(
        { value: shortUrlRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      originalUrl: new FormControl(shortUrlRawValue.originalUrl),
      shortCode: new FormControl(shortUrlRawValue.shortCode),
      createdAt: new FormControl(shortUrlRawValue.createdAt),
      expiryAt: new FormControl(shortUrlRawValue.expiryAt),
      active: new FormControl(shortUrlRawValue.active),
      accessCount: new FormControl(shortUrlRawValue.accessCount),
    });
  }

  getShortUrl(form: ShortUrlFormGroup): IShortUrl | NewShortUrl {
    return form.getRawValue() as IShortUrl | NewShortUrl;
  }

  resetForm(form: ShortUrlFormGroup, shortUrl: ShortUrlFormGroupInput): void {
    const shortUrlRawValue = { ...this.getFormDefaults(), ...shortUrl };
    form.reset(
      {
        ...shortUrlRawValue,
        id: { value: shortUrlRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ShortUrlFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}

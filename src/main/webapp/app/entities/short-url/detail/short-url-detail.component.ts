import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IShortUrl } from '../short-url.model';

@Component({
  selector: 'jhi-short-url-detail',
  templateUrl: './short-url-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class ShortUrlDetailComponent {
  shortUrl = input<IShortUrl | null>(null);

  previousState(): void {
    window.history.back();
  }
}

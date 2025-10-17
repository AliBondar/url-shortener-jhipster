import { Component } from '@angular/core';
import { ShortUrlService, ShortenRequestDTO } from '../short-url.service';

@Component({
  selector: 'app-short-url',
  templateUrl: './short-url.component.html',
  standalone: true,
  styleUrls: ['./short-url.component.css'],
})
export class ShortUrlComponent {
  originalUrl = '';
  shortUrl = '';
  shortCode = '';
  originalFromShort = '';

  constructor(private shortUrlService: ShortUrlService) {}

  shorten(): void {
    const request: ShortenRequestDTO = { originalUrl: this.originalUrl };
    this.shortUrlService.shortenUrl(request).subscribe({
      next: res => (this.shortUrl = res.shortUrl),
      error: err => console.error(err),
    });
  }

  getOriginal(): void {
    this.shortUrlService.getOriginalUrl(this.shortCode).subscribe({
      next: res => (this.originalFromShort = res),
      error: err => console.error(err),
    });
  }
}

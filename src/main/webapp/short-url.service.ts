import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ShortenRequestDTO {
  originalUrl: string;
}

export interface ShortenResponseDTO {
  shortUrl: string;
}

@Injectable({
  providedIn: 'root',
})
export class ShortUrlService {
  private apiUrl = '/api/short-urls';

  constructor(private http: HttpClient) {}

  shortenUrl(request: ShortenRequestDTO): Observable<ShortenResponseDTO> {
    return this.http.post<ShortenResponseDTO>(`${this.apiUrl}/shorten`, request);
  }

  getOriginalUrl(shortCode: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/get-short-url/${shortCode}`, { responseType: 'text' });
  }
}

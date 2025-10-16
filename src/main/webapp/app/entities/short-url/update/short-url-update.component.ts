import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShortUrl } from '../short-url.model';
import { ShortUrlService } from '../service/short-url.service';
import { ShortUrlFormGroup, ShortUrlFormService } from './short-url-form.service';

@Component({
  selector: 'jhi-short-url-update',
  templateUrl: './short-url-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ShortUrlUpdateComponent implements OnInit {
  isSaving = false;
  shortUrl: IShortUrl | null = null;

  protected shortUrlService = inject(ShortUrlService);
  protected shortUrlFormService = inject(ShortUrlFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShortUrlFormGroup = this.shortUrlFormService.createShortUrlFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shortUrl }) => {
      this.shortUrl = shortUrl;
      if (shortUrl) {
        this.updateForm(shortUrl);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shortUrl = this.shortUrlFormService.getShortUrl(this.editForm);
    if (shortUrl.id !== null) {
      this.subscribeToSaveResponse(this.shortUrlService.update(shortUrl));
    } else {
      this.subscribeToSaveResponse(this.shortUrlService.create(shortUrl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShortUrl>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(shortUrl: IShortUrl): void {
    this.shortUrl = shortUrl;
    this.shortUrlFormService.resetForm(this.editForm, shortUrl);
  }
}

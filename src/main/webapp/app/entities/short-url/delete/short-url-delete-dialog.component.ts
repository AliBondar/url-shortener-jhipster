import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IShortUrl } from '../short-url.model';
import { ShortUrlService } from '../service/short-url.service';

@Component({
  templateUrl: './short-url-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ShortUrlDeleteDialogComponent {
  shortUrl?: IShortUrl;

  protected shortUrlService = inject(ShortUrlService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shortUrlService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

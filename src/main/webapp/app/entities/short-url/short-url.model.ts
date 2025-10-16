import dayjs from 'dayjs/esm';

export interface IShortUrl {
  id: number;
  originalUrl?: string | null;
  shortCode?: string | null;
  createdAt?: dayjs.Dayjs | null;
  expiryAt?: dayjs.Dayjs | null;
  active?: boolean | null;
  accessCount?: number | null;
}

export type NewShortUrl = Omit<IShortUrl, 'id'> & { id: null };

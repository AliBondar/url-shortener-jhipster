import dayjs from 'dayjs/esm';

import { IShortUrl, NewShortUrl } from './short-url.model';

export const sampleWithRequiredData: IShortUrl = {
  id: 14870,
};

export const sampleWithPartialData: IShortUrl = {
  id: 26308,
  originalUrl: 'consequently',
  createdAt: dayjs('2025-10-15'),
  accessCount: 31025,
};

export const sampleWithFullData: IShortUrl = {
  id: 21437,
  originalUrl: 'if confound',
  shortCode: 'loaf oxidize starch',
  createdAt: dayjs('2025-10-15'),
  expiryAt: dayjs('2025-10-15'),
  active: true,
  accessCount: 28929,
};

export const sampleWithNewData: NewShortUrl = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

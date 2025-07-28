import { DATE_FORMAT } from 'app/constants/common';
import dayjs, { Dayjs } from 'dayjs';

const DEFAULT_HIGHLIGHT_DAY = 25;

export function getBirthdayDateRange(currentDate: string) {
  const date = dayjs(currentDate);
  const day = date.date();

  let fromDate: Dayjs, toDate: Dayjs;

  if (day >= DEFAULT_HIGHLIGHT_DAY) {
    fromDate = date.date(DEFAULT_HIGHLIGHT_DAY);
    toDate = date.add(1, 'month').endOf('month');
  } else {
    fromDate = date.date(1);
    toDate = date.endOf('month');
  }

  return {
    fromDate: fromDate.format(DATE_FORMAT.YEAR_DATE),
    toDate: toDate.format(DATE_FORMAT.YEAR_DATE),
  };
}

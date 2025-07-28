import dayjs, { Dayjs, ManipulateType } from 'dayjs';

import { APP_LOCAL_DATETIME_FORMAT } from 'app/config/constants';
import { DATE_FORMAT } from 'app/constants/common';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { BasicSelect } from '../model/arr-obj.model';

export const convertDateTimeFromServer = date =>
  date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null;

export const convertDateTimeToServer = (date?: string): dayjs.Dayjs | null =>
  date ? dayjs(date) : null;

export const displayDefaultDateTime = () =>
  dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export function getAllDatesInMonth(
  year: number,
  month: number,
): { dayOfWeek: string; dateNumber: number; fullDate: string }[] {
  const startDate = new Date(year, month - 1, 1); // Month is zero-based, so subtract 1
  const endDate = new Date(year, month, 0); // Get the last day of the month

  const dates: { dayOfWeek: string; dateNumber: number; fullDate: string }[] =
    [];
  for (
    let date = startDate;
    date <= endDate;
    date.setDate(date.getDate() + 1)
  ) {
    const dateNumber = date.getDate();
    const dayOfWeek = getDayOfWeek(date);
    dates.push({
      dayOfWeek,
      dateNumber,
      fullDate: dayjs(date).format(DATE_FORMAT.YEAR_DATE),
    });
  }
  return dates;
}

function getDayOfWeek(date: Date): string {
  const days: string[] = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
  return days[date.getDay()];
}

export interface DayInfo {
  day: string;
  date: number;
  fullDate: string;
}

export function getAllDaysInWeek(currentDate: dayjs.Dayjs): DayInfo[] {
  const startOfWeek = currentDate.startOf('week');
  const endOfWeek = currentDate.endOf('week');

  const daysInWeek: DayInfo[] = [];
  let currentDay = startOfWeek;

  const vietnameseDays: Record<string, string> = {
    Sunday: 'CN',
    Monday: 'T2',
    Tuesday: 'T3',
    Wednesday: 'T4',
    Thursday: 'T5',
    Friday: 'T6',
    Saturday: 'T7',
  };

  while (
    currentDay.isBefore(endOfWeek) ||
    currentDay.isSame(endOfWeek, 'day')
  ) {
    const dayOfWeek = currentDay.format('dddd');
    const vietnameseDay = vietnameseDays[dayOfWeek];
    daysInWeek.push({
      day: vietnameseDay,
      date: currentDay.date(),
      fullDate: currentDay.format(DATE_FORMAT.YEAR_DATE),
    });
    currentDay = currentDay.add(1, 'day');
  }

  return daysInWeek;
}

export function getStartAndEndOfWeek(
  date: string | Date,
  format?: string,
): { startOfWeek: string; endOfWeek: string } {
  const startOfWeek = dayjs(date)
    .startOf('week')
    .format(format || DATE_FORMAT.DATE);
  const endOfWeek = dayjs(date)
    .endOf('week')
    .format(format || DATE_FORMAT.DATE);

  return { startOfWeek, endOfWeek };
}

export function getStartAndEndOfMonth(
  date: string | Date,
  format?: string,
): { startOfMonth: string; endOfMonth: string } {
  const startOfMonth = dayjs(date)
    .startOf('month')
    .format(format || DATE_FORMAT.DATE);
  const endOfMonth = dayjs(date)
    .endOf('month')
    .format(format || DATE_FORMAT.DATE);

  return { startOfMonth, endOfMonth };
}

export const convertToIsoDate = (date: DateObject) =>
  date?.toDate().toISOString();

export const convertToDate = (date: DateObject) => date?.toDate();

export function dayjsRange(
  start: Dayjs,
  end: Dayjs,
  unit: ManipulateType,
): Dayjs[] {
  const range = [];
  let current = start;
  while (!current.isAfter(end)) {
    range.push(current);
    current = current.add(1, unit);
  }
  return range;
}

export function dayjsRangeSelect(
  start: Dayjs,
  end: Dayjs,
  unit: ManipulateType,
) {
  const range = [];
  const objRange = {}
  let current = start;
  while (!current.isAfter(end)) {
    let format = current.format('MM/YYYY');
    let tmp = { label: format, value: format }
    range.push(tmp);
    objRange[format] = tmp;
    current = current.add(1, unit);
  }
  return { arr: range, obj: objRange };
}

export const durationMonth = (start: string | Date, end: string | Date) => {
  const startDate = dayjs(start);
  const endDate = dayjs(end);
  const rangeMonth = dayjsRange(startDate, endDate, 'month');
  return rangeMonth.map((date, index) => {
    let startRange = startDate;
    let endRange = endDate;
    switch (index) {
      case 0:
        endRange = date.endOf('month');
        break;
      case rangeMonth.length - 1:
        startRange = date.startOf('month');
        break;
      default:
        startRange = date.startOf('month');
        endRange = date.endOf('month');
        break;
    }
    return {
      labelMonth: 'Tháng ' + date.format('MM'),
      sizeDay: dayjsRange(startRange, endRange, 'day').length,
    };
  });
};

export const eventDateCalendar = (iso: string, isDuplicate: boolean) => {
  let [year, month, day, hour, minute, second, miliSecond] = dayjs(iso)
    .format('YYYY-MM-DD-HH-mm-ss-SSS')
    .split('-');
  if (isDuplicate) {
    miliSecond = (+miliSecond + 1) as any;
    if (+miliSecond === 999) {
      second = (+second + 1) as any;
      miliSecond = (+miliSecond - 1) as any;
    }
  }
  return new Date(+year, +month, +day, +hour, +minute, +second, +miliSecond);
};

export const handleMergeTime = (date: dayjs.Dayjs | null) => {
  if (date) {
    const currentTime = dayjs();

    const mergedDateTime = date
      .hour(currentTime.hour())
      .minute(currentTime.minute())
      .second(currentTime.second())
      .millisecond(currentTime.millisecond());
    return mergedDateTime;
  }
};

export const startAndEndOfCalendar = (date: dayjs.Dayjs) => {
  const start = date.startOf('month').startOf('week');
  const end = date.endOf('month').endOf('week');
  return { start, end };
}

export const formatDateYear = (date: string) => {
  if (!date?.includes('-')) return date;
  let [year, month, day] = date.split('-');
  return new Date(+year, +month - 1, +day).toISOString();
}

export const checkIsoDate = (date: string) => {
  const schema = z.string().transform((str) => new Date(str))
  return schema.safeParse(date).success
}

export const checkFormatDateYear = (date: string) => {
  const dateRegex = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\d|3[0-1])$/;
  const schema = z.string().regex(dateRegex, "Invalid date format (YYYY-MM-DD)");
  return schema.safeParse(date).success
}
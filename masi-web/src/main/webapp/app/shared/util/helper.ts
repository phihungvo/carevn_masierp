import dayjs, { Dayjs } from 'dayjs';

// Lấy thứ trong tuần T2 - T3 - T4 .... sử dụng custom locale
export const dayInWeek = (date: Date) => dayjs(date).format('dd');

export const getDateRange = (startDate: Dayjs, endDate: Dayjs) => {
  const start = dayjs(startDate.toDate());
  const end = dayjs(endDate.toDate());
  const dateArray: { iso: string; shortName: string; dayInMonth: number }[] = [];

  let currentDate = start;

  while (currentDate.isBefore(end) || currentDate.isSame(end, 'day')) {
    dateArray.push({
      iso: currentDate.toISOString(),
      shortName: dayjs(currentDate).locale('vi').format('dd'),
      dayInMonth: dayjs(currentDate).date(),
    });
    currentDate = currentDate.add(1, 'day');
  }

  return dateArray;
};

export const getDateRangeCalendar = (startDate: Dayjs, endDate: Dayjs) => {
  const start = dayjs(startDate.toDate());
  const end = dayjs(endDate.toDate());
  const dateArray: {
    iso: string;
    shortName: string;
    dayInMonth: number;
    format: string;
    isToday: boolean;
  }[] = [];

  let currentDate = start;

  while (currentDate.isBefore(end) || currentDate.isSame(end, 'day')) {
    dateArray.push({
      iso: currentDate.toISOString(),
      shortName: dayjs(currentDate).locale('vi').format('dd'),
      dayInMonth: dayjs(currentDate).date(),
      format: dayjs(currentDate).format('YYYY-MM-DD'),
      isToday: dayjs(currentDate).isSame(dayjs(), 'day'),
    });
    currentDate = currentDate.add(1, 'day');
  }

  return dateArray;
};

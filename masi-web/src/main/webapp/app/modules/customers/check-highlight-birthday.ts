import dayjs from 'dayjs';

const DEFAULT_HIGHLIGHT_DAY = 25;
// 25/8 -> show highlight from 25/8 to all days in September
// 1/9 -> show highlight from 1/9 to all days in September
// 25/9 -> show highlight from 25/9 to all days in October

export const checkHighlightBirthday = (birthday: string) => {
  const currentDate = dayjs().date();
  const isCurrentMonth = dayjs(birthday).month() === dayjs().month();
  const isGap1Month = birthday && dayjs(birthday).month() - dayjs().month() === 1;

  if (isCurrentMonth && currentDate < DEFAULT_HIGHLIGHT_DAY) {
    return true;
  }

  if (isCurrentMonth && currentDate >= DEFAULT_HIGHLIGHT_DAY) {
    return dayjs(birthday).date() >= DEFAULT_HIGHLIGHT_DAY;
  }

  if (isGap1Month && currentDate >= DEFAULT_HIGHLIGHT_DAY) {
    return true;
  }

  return false;
};

export const generateBirthdayDate = (month: number): { birthdayFrom: string; birthdayTo: string } => {
  const currentYear = dayjs().year();

  return {
    birthdayFrom: `${currentYear}-${month < 10 ? 0 : ''}${month}-01`,
    birthdayTo: `${currentYear}-${month < 10 ? 0 : ''}${month}-${dayjs(`${currentYear}-${month < 10 ? 0 : ''}${month}-01`)
      .endOf('month')
      .date()}`,
  };
};

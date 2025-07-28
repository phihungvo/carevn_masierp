import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import updateLocale from 'dayjs/plugin/updateLocale';

dayjs.extend(customParseFormat);
dayjs.extend(updateLocale);

dayjs.updateLocale('vi', {
  weekdaysMin: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
});

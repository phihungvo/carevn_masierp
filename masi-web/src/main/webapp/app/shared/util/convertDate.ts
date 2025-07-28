import { DateObject } from 'react-multi-date-picker';

export const convertDate = (dateFrom: DateObject, DataTo: DateObject): boolean => {
  const dateFromDay = dateFrom.toDate().getDate();
  const dateFromMonth = dateFrom.toDate().getMonth();
  const dateFromYear = dateFrom.toDate().getFullYear();
  const DataToToDay = DataTo.toDate().getDate();
  const DataToToMonth = DataTo.toDate().getMonth();
  const DataToToYear = DataTo.toDate().getFullYear();

  if (dateFromYear < DataToToYear) return true;
  else if (dateFromMonth < DataToToMonth) return true;
  else if (dateFromDay < DataToToDay) return true;
  else return false;
};

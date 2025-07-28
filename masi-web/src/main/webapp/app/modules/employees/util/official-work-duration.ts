import dayjs from "dayjs";

interface IGetWorkDurationDisplayProps {
  startWorkDate: dayjs.Dayjs | Date | string | undefined
  probationEndDate: dayjs.Dayjs | Date | string | undefined
}

export function getWorkDurationDisplay({ startWorkDate, probationEndDate }: IGetWorkDurationDisplayProps): string {
  let finalDay: dayjs.Dayjs | undefined;
  // if (probationEndDate && dayjs(probationEndDate).isValid()) {
  //   finalDay = dayjs(probationEndDate);
  // }else {
    finalDay = dayjs(startWorkDate);
  // }s
  if (!finalDay.isValid()) {
    return ""
  }
  const yearGap = dayjs().diff(finalDay, 'year');
  const monthGap = dayjs().diff(finalDay, 'month') - yearGap * 12;
  if (monthGap == 0 && yearGap == 0) {
    return "0"
  }
  if (monthGap == 0) {
    return `${yearGap} năm`
  }
  if (yearGap == 0) {
    return `${monthGap} tháng`
  }
  return `${yearGap} năm ${monthGap} tháng`
}

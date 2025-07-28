export function generateHoursArray(): string[] {
  const hoursArray: string[] = [];
  for (let hour = 0; hour <= 24; hour++) {
    for (let minute = 0; minute < 60; minute += 30) {
      hoursArray.push(`${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`);
    }
  }
  return hoursArray;
}

export function hourToMinutes(hour: string): number {
  const [hh, mm] = hour.split(':').map(Number);
  return hh * 60 + mm;
}

// Function to compare two selected hour options
export function compareSelectedHours(hour1: string, hour2: string): boolean {
  if (!hour1 || !hour2) {
    return true;
  }

  const minutes1 = hourToMinutes(hour1);
  const minutes2 = hourToMinutes(hour2);

  if (minutes1 < minutes2) {
    return true;
  } else if (minutes1 > minutes2) {
    return false;
  }
}

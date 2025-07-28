export function getCurrentDateMonth(month: number): { date: string; month: number } {
  const today = new Date();
  const year = today.getFullYear();
  const day = today.getDate();

  // Months in JavaScript Date object are 0-indexed (0 for January, 11 for December)
  const date = new Date(year, month - 1, day);

  // Format date as YYYY-MM-DD
  const formattedDate = date.toISOString().split('T')[0];
  return { date: formattedDate, month };
}

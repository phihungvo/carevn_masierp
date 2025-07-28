export function isNonNegativeNumber(str: string) {
  // Regular expression to match non-negative numbers with a minimum value of 0
  const regex = /^(?:|[1-9]\d*)(?:\.\d+)?$/;

  // Test if the string matches the regular expression
  return regex.test(str);
}

export function isValidDateObject(value: any): boolean {
  // Implement your validation logic here
  // For example, check if the value is an object with 'year', 'month', and 'day' properties
  return typeof value === 'object' && value !== null && 'year' in value && 'month' in value && 'day' in value;
}

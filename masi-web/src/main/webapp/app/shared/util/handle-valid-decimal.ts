import { Path } from 'react-hook-form';

export const handleValidDecimal = <T extends object = {}>(
  value: string,
  name: Path<T>,
  regex: RegExp,
  setValue: (name: Path<T>, value: string) => void,
) => {
  const decimalRegex = regex;

  if (decimalRegex.test(value) || value === '') {
    setValue(name, value);
  } else {
    setValue(name, value.slice(0, -1).replace(',', '.'));
  }
};

export const handleValidatePaste = (e: React.ClipboardEvent<HTMLInputElement>, regex: RegExp) => {
  const clipboardData = e.clipboardData || window['clipboardData'];
  const pastedData = clipboardData.getData('text');

  const decimalRegex = regex;

  if (!decimalRegex.test(pastedData)) {
    e.preventDefault();
  }
};

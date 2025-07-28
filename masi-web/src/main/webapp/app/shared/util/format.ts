export const convertCurrency = (value: number, isPrefix = true) => {
  value = value || 0;
  if (!isPrefix) {
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 0,
    }).format(value);
  }
  return `${new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 0,
  }).format(value)}`;
};

export const convertCurrencyWithMaximum = (max?: number) => (value: number, isPrefix = true) => {
  value = value || 0;
  value = Math.min(value, max || Infinity)
  if (!isPrefix) {
    return new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 0,
    }).format(value);
  }
  return `${new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 0,
  }).format(value)}`;
};

export const convertDigitalNumber = (value: number, digit = 0) => {
  return new Intl.NumberFormat('en', {
    notation: 'compact',
    maximumFractionDigits: digit,
  }).format(value);
};

export const camelToSnakeCase = (str: string) =>
  str.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);

export const convertToVietnameseCurrency = (number: number = 0) => {
  const units = [
    '',
    'mươi',
    'trăm',
    'nghìn',
    'triệu',
    'tỷ',
    'triệu',
    'tỷ',
    'tỷ',
    'tỷ',
  ];
  const numberToText = [
    'không',
    'một',
    'hai',
    'ba',
    'bốn',
    'năm',
    'sáu',
    'bảy',
    'tám',
    'chín',
  ];

  // Hàm xử lý từng nhóm 3 chữ số
  function readTriple(number) {
    const hundred = Math.floor(number / 100);
    const ten = Math.floor((number % 100) / 10);
    const unit = number % 10;

    let result = '';
    if (hundred > 0) {
      result += `${numberToText[hundred]} trăm `;
    }
    if (ten > 0) {
      if (ten === 1) {
        result += 'mười ';
      } else {
        result += `${numberToText[ten]} mươi `;
      }
    } else if (unit > 0 && hundred > 0) {
      result += 'lẻ ';
    }

    if (unit > 0) {
      if (unit === 1 && ten > 1) {
        result += 'mốt ';
      } else if (unit === 5 && ten > 0) {
        result += 'lăm ';
      } else {
        result += `${numberToText[unit]} `;
      }
    }

    return result.trim();
  }

  // Chia thành các nhóm 3 chữ số
  let numStr = number.toString();
  const groups = [];
  while (numStr.length > 0) {
    groups.unshift(parseInt(numStr.slice(-3), 10));
    numStr = numStr.slice(0, -3);
  }

  // Đọc từng nhóm
  let result = '';
  for (let i = 0; i < groups.length; i++) {
    const groupValue = groups[i];
    const unitIndex = groups.length - i - 1;

    if (groupValue > 0) {
      result += `${readTriple(groupValue)} ${units[unitIndex * 3]} `;
    }
  }

  return result.charAt(0).toUpperCase() + result.slice(1).trim() + ' đồng';
};

export const iconPath = (icon_name: string) => {
  return 'content/images/vuesax/linear/' + icon_name;
};

export const generateMonthYearOptions = (
  startYear,
  startMonth,
  endYear,
  endMonth,
) => {
  const list = [];
  let year = startYear;
  let month = startMonth;

  while (year < endYear || (year === endYear && month <= endMonth)) {
    const formattedMonth = month.toString().padStart(2, '0'); // Ensure two-digit month
    list.push(`${formattedMonth}/${year}`);

    month++;
    if (month > 12) {
      month = 1;
      year++;
    }
  }

  return list?.map(x => ({ value: x, label: x }));
};

export const format2Digit = (value: number) => {
  return Math.round(value * 100) / 100;
}

import { Permission } from "app/config/permission";
import { Path } from "react-hook-form";

export const DATE_FORMAT = {
  DATE_TIME: 'YYYY-MM-DD HH:mm:ss',
  DATE: 'DD/MM/YYYY',
  DATE_STRING: '[Ngày] DD [Tháng] MM [Năm] YYYY',
  YEAR_DATE: 'YYYY-MM-DD',
  TIME: 'HH:mm:ss',
  TIME_ONLY: 'HH:mm',
  MONTH_ONLY: 'MM',
  TIME_DATE: 'HH:mm DD/MM/YYYY',
  YEAR: 'YYYY',
  MONTH_YEAR: 'MMYY',
  DATE_TIME_ONLY: 'YYYY-MM-DD HH:mm',
  DATE_TIME_2: 'DD/MM/YYYY HH:mm',
};

export const weekDays = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
// export const weekDays = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
// export const weekDays = ['Chủ nhật', 'Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7'];
export const monthNames = [
  'Tháng 1',
  'Tháng 2',
  'Tháng 3',
  'Tháng 4',
  'Tháng 5',
  'Tháng 6',
  'Tháng 7',
  'Tháng 8',
  'Tháng 9',
  'Tháng 10',
  'Tháng 11',
  'Tháng 12',
];

export const DEFAULT_MAX_HOUR_WORK = 8;

export const DEFAULT_PAGE = 0;
export const DEFAULT_PAGE_SIZE = 10;
export const DEFAULT_PAGE_SIZE_MATERIALS = 15;
export const DEFAULT_PAGE_SIZE_NAX = 2147483647;

export const DEFAULT_MAX_REQUEST_APPROVE = 4;

export const TIME_SHEET_DECIMAL_REGEX = /^\d{1,2}(\.\d{0,2})?$/;
export const TIME_SHEET_BULK_DECIMAL_REGEX =
  /^\d{1,3}(\.\d{0,3})?$|^[oO][fF]?[fF]?$|^[pP]?$|^[pP][oO]?$|^[nN][bB]?$|^[vV]?$|^[kK][pP]?$|^[wW][ff]?[hH]?$/i;
export const DEFAULT_DECIMAL_REGEX = /^\d+(\.\d*)?$/;
export const DEFAULT_INTEGER_REGEX = /^[0-9]\d*$/;
export const DEFAULT_NUMBER_REGEX = /^[0-9]*$/;

export const VIETNAMESE_PHONE_NUMBER_REGEX =
  /(84|0[2|3|5|7|8|9])+([0-9]{8,9})\b/g;

export const FILE_UTIL = `${window.location.protocol}//${window.location.host}/services/masiutility/api/file-attachments`;

export const ICON_PATH = 'content/images/vuesax/linear/';

export const paramsExportExcel = {
  params: {
    download: true,
  },
  responseType: 'blob',
  headers: {
    Accept: 'application/octet-stream',
  },
}


export const checkPermissionAction = (authorities: string[], key: string) => {
  const isSuperAdmin = authorities?.indexOf('ROLE_SUPER_ADMIN') != -1;
  const isAdmin = authorities?.indexOf('ROLE_ADMIN') != -1;

  if (isSuperAdmin || isAdmin) {
    return true;
  }
  return key ? authorities?.indexOf(`PERMISSION.${key}`) !== -1 : true;
}

export const isHasPermission = (authorities: string[], key: Path<Permission>) => {
  const isSuperAdmin = authorities?.indexOf('ROLE_SUPER_ADMIN') != -1;
  const isAdmin = authorities?.indexOf('ROLE_ADMIN') != -1;
  return isSuperAdmin || isAdmin || authorities?.indexOf(`PERMISSION.${key}`) !== -1;
}

export const isHasPermissionList = (authorities: string[], keys: Path<Permission>[]) => {
  const isSuperAdmin = authorities?.indexOf('ROLE_SUPER_ADMIN') != -1;
  const isAdmin = authorities?.indexOf('ROLE_ADMIN') != -1;
  let keysLength = 0
  keys.forEach(key => {
    if (authorities?.indexOf(`PERMISSION.${key}`) !== -1) {
      keysLength++
    }
  })
  return isSuperAdmin || isAdmin || keysLength === keys.length
}
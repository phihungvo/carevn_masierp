const DEFAULT_MAX_FILE_NAME_LENGTH = 6;

export function shortenFileName(fileName: string) {
  if (!fileName) {
    return '';
  }

  if (fileName.length <= DEFAULT_MAX_FILE_NAME_LENGTH) {
    return fileName;
  }

  const splittedFileName = fileName.split('.');
  const shortenedFileName = splittedFileName[0].slice(0, DEFAULT_MAX_FILE_NAME_LENGTH) + '...' + `.${splittedFileName[1]}`;

  return shortenedFileName;
}

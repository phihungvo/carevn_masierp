import { RecordType } from 'app/components/table/table.d';

export const isSubsetArrayString = (arr1: string[], arr2: string[]): boolean => {
  return arr2?.every(item => arr1?.includes(item));
};

export const getDistinctObjects = <T extends object>(array: Array<T | RecordType<T>>, key: keyof T): Array<T | RecordType<T>> => {
  const uniqueObjects = array.reduce((acc, current) => {
    const keyValue = current[key];
    if (!acc.has(keyValue)) {
      acc.set(keyValue, current);
    }
    return acc;
  }, new Map());

  return Array.from(uniqueObjects.values());
};

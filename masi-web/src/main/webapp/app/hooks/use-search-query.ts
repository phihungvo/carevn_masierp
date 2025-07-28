import React, { useEffect, useState } from 'react';

type Props<T> = {
  defaultValue: {
    query?: T;
    search?: T;
  };
  howToResolveData?: (key: keyof T, value: any) => void;
  timeout?: number;
};

const useSearchQuery = <T extends object>(props: Props<T>) => {
  const { defaultValue, howToResolveData, timeout = 500 } = props;

  const [query, setQuery] = useState<T>(defaultValue?.query || ({} as T));
  const [search, setSearch] = useState<T>(defaultValue?.search || ({} as T));

  const handleQuery = (key: keyof T) => (eventData: any) => {
    let data = undefined
    if (howToResolveData) {
      data = howToResolveData(key, eventData);
    }
    setQuery({ ...query, [key]: data });
  };

  const handleSearch = (key: keyof T) => (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(pre => ({ ...pre, [key]: e?.target?.value }));
  };

  useEffect(() => {
    let timeout_search = setTimeout(() => {
      setQuery({ ...query, ...search });
    }, timeout);
    return () => clearTimeout(timeout_search);
  }, [search]);

  return {
    handleSearch,
    handleQuery,
    query,
    search,
    setQuery,
  };
};

export default useSearchQuery;

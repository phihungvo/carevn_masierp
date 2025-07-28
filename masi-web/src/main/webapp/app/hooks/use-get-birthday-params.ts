import { DEFAULT_PAGE } from 'app/constants/common';
import { ICustomerParams } from 'app/shared/model/customer.model';
import { useEffect } from 'react';

export function useGetBirthdayParams(queryString: string, setFilter: React.Dispatch<React.SetStateAction<ICustomerParams>>) {
  useEffect(() => {
    // Create a URLSearchParams object to easily access the query parameters
    const params = new URLSearchParams(queryString);

    // Get the values of birthdayFrom and birthdayTo
    const birthdayFrom = params.get('birthdayFrom') ?? '';
    const birthdayTo = params.get('birthdayTo') ?? '';

    if (birthdayFrom && birthdayTo) {
      setFilter(prev => ({
        ...prev,
        birthdayFrom,
        birthdayTo,
        page: DEFAULT_PAGE,
      }));
    }
  }, [queryString]);
}

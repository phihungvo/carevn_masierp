import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import useSearchQuery from 'app/hooks/use-search-query';
import { Filter } from './types/Filter';
import { DateObject } from 'react-multi-date-picker';

const { useIncomingInvoices } = useIncomingInvoice;

type Props = {}

const useList = (props: Props) => {
  const { setQuery, search, handleSearch, query, handleQuery } = useSearchQuery<Partial<Filter>>({
    defaultValue: {
      query: { page: 0, size: 10 },
    },
    howToResolveData: (key, value) => {
      switch (key) {
        default:
          return value;
      }
    }
  })

  const incoming_invoice_query = useIncomingInvoices({
    ...query,
    startDate: query.startDate ? new DateObject(query.startDate).format('YYYY-MM-DD'): null,
    endDate: query.endDate ? new DateObject(query.endDate).format('YYYY-MM-DD') : null,
  });

  return {
    setQuery,
    search,
    handleSearch,
    handleQuery,
    incommingList: incoming_invoice_query,
    query
  }
}

export default useList

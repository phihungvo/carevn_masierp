import CardV2 from 'app/components/CardV2/CardV2';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IPaymentRequestParams } from 'app/shared/model/payment-request.model';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import RequestPaymentHeader from './request-payment-header';
import RequestPaymentHeaderTable from './request-payment-header-table';
import RequestPaymentTable from './request-payment-table';
import './request-payment.scss';
import RequestPaymentFilterModals from './request-payment-filter-modals';

const defaultQuery = {
  page: DEFAULT_PAGE,
  size: DEFAULT_PAGE_SIZE,
  search: '',
};

const RequestPayment = () => {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState<IPaymentRequestParams>(defaultQuery);
  const [openFilter, setOpenFilter] = useState<boolean>(false);

  const toggleFilter = () => setOpenFilter(prev => !prev);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) =>
    setSearch(e.target.value);

  useEffect(() => {
    const setSearchToQuery = setTimeout(
      () => setFilter({ ...filter, search }),
      300,
    );

    return () => clearTimeout(setSearchToQuery);
  }, [search]);

  return (
    <>
      <CardV2 header={<RequestPaymentHeader />}>
        <div className="rp__container">
          <RequestPaymentHeaderTable
            handleSearch={handleSearch}
            toggleFilter={toggleFilter}
            search={search}
          />

          <RequestPaymentTable
            filter={filter}
            setFilter={setFilter}
            searchText={search}
          />
        </div>
      </CardV2>

      <RequestPaymentFilterModals
        isOpen={openFilter}
        toggle={toggleFilter}
        setFilter={setFilter}
      />
    </>
  );
};

export default RequestPayment;

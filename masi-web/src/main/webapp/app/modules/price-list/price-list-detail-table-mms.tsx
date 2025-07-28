import React, { useEffect } from 'react';
import Table from 'app/components/table/table';
import { IQuotation, IQuotationDetail } from 'app/shared/model/quotation.model';
import { useFormContext } from 'react-hook-form';
import { generateColumnsDetailsMMS } from './generate-columns-detail-mms';

interface IPriceListDetailTableMMSProps {
  data?: IQuotation;
}

const PriceListDetailTableMMS = ({ data }: IPriceListDetailTableMMSProps) => {
  const columns = generateColumnsDetailsMMS();

  const { setValue, watch } = useFormContext();

  useEffect(() => {
    if (data) {
      setValue(`quotationDetails`, data?.quotationDetails);
    }
  }, [data]);

  return <Table<IQuotationDetail> dataSource={watch('quotationDetails')} columns={columns} />;
};

export default PriceListDetailTableMMS;

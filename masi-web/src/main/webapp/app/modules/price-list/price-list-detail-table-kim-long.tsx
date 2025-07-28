import React, { useEffect } from 'react';
import Table from 'app/components/table/table';
import { IQuotation, IQuotationDetail } from 'app/shared/model/quotation.model';
import { FieldArrayWithId, useFormContext } from 'react-hook-form';
import { generateColumnsDetailsKimLong } from './generate-columns-detail-kim-long';

interface IPriceListDetailTableKimLongProps {
  data?: IQuotation;
}

const PriceListDetailTableKimLong = ({ data }: IPriceListDetailTableKimLongProps) => {
  const columns = generateColumnsDetailsKimLong();

  const { setValue, watch } = useFormContext();

  useEffect(() => {
    if (data) {
      setValue(`quotationDetails`, data?.quotationDetails);
    }
  }, [data]);

  return <Table<IQuotationDetail> rowKey="id" dataSource={watch('quotationDetails') || []} columns={columns} />;
};

export default PriceListDetailTableKimLong;

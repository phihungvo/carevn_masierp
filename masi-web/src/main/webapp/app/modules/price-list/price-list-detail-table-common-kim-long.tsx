import { IQuotation } from 'app/shared/model/quotation.model';
import React from 'react';
import Table from 'app/components/table/table';
import { generateColumnsDetailCommonKimLong } from './generate-columns-detail-common-kim-long';

interface IPriceListDetailTableProps {
  data: IQuotation;
}

const PriceListDetailTableCommonKimLong = (props: IPriceListDetailTableProps) => {
  const { data } = props;

  const columns = generateColumnsDetailCommonKimLong();

  if (!data) return null;

  return <Table<IQuotation> rowKey="id" dataSource={[data]} columns={columns} />;
};

export default PriceListDetailTableCommonKimLong;

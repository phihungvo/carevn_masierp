import { IQuotation } from 'app/shared/model/quotation.model';
import React from 'react';
import Table from 'app/components/table/table';
import { generateColumnsDetailCommonMMS } from './generate-columns-detail-common-mms';

interface IPriceListDetailTableProps {
  data: IQuotation;
}

const PriceListDetailTableCommonMMS = (props: IPriceListDetailTableProps) => {
  const { data } = props;

  const columns = generateColumnsDetailCommonMMS();

  if (!data) return null;

  return <Table<IQuotation> rowKey="id" dataSource={[data]} columns={columns} />;
};

export default PriceListDetailTableCommonMMS;

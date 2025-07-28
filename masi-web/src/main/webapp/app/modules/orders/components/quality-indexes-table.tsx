import React, { ReactElement } from 'react';

import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IOrder } from 'app/shared/model/order.model';
import { generateColumnsQualityIndexes } from '../generate-columns-quality-indexes';

interface IQualityIndexesTableProps {
  list: IOrder['qualityIndexes'];
}

function QualityIndexesTable({ list }: IQualityIndexesTableProps): ReactElement {
  const columns: ColumnsTypes<{ name: string; value: string }> = generateColumnsQualityIndexes();
  return <Table columns={columns} dataSource={list} responsive={false} />;
}

export default QualityIndexesTable;

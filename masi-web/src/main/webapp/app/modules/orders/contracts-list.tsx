import React, { ReactElement } from 'react';

import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IContractMaterial } from 'app/shared/model/contract.model';
import { generateColumnsContract } from 'app/modules/orders/generate-columns-contract';

interface IContractsListProps {
  list: IContractMaterial[];
  selectedMaterialsKeys?: string[];
  setSelectedMaterialsKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

function ContractsList({ list = [], selectedMaterialsKeys, setSelectedMaterialsKeys }: IContractsListProps): ReactElement {
  const columns: ColumnsTypes<IContractMaterial> = generateColumnsContract();

  return (
    <Table<IContractMaterial>
      // {...(selectedMaterialsKeys &&
      //   selectedMaterialsKeys && {
      //   rowSelection: {
      //     type: 'checkbox',
      //     onChange: (selectedRowKeys, selectedRecords) => {
      //       setSelectedMaterialsKeys(selectedRowKeys);
      //     },
      //     selectedRowKeys: selectedMaterialsKeys,
      //     disabledKeys: selectedMaterialsKeys,
      //   },
      // })}
      rowKey="id"
      columns={columns}
      dataSource={list}
      responsive={false}
    />
  );
}

export default ContractsList;

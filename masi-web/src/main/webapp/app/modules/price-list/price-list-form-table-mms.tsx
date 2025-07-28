import React, { useState } from 'react';
import { generateColumnsMMS } from './generate-columns-mms';
import Table from 'app/components/table/table';
import { IQuotation, IQuotationDetail } from 'app/shared/model/quotation.model';
import { FieldArrayWithId, useFieldArray, useFormContext } from 'react-hook-form';
import PriceListDeleteMaterialModals from './modals/price-list-delete-material-modals';

interface IPriceListDetailTableMMSProps {
  data?: IQuotation;
}

const PriceListFormTableMMS = ({ data }: IPriceListDetailTableMMSProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState<number>(0);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };
  const columns = generateColumnsMMS(data?.status, toggle, setSelectedIndex);

  const { control, watch } = useFormContext();
  const { remove } = useFieldArray({
    control,
    name: 'quotationDetails',
  });

  const onOk = () => {
    remove(selectedIndex);
    toggle();
  };

  const onCancel = () => {
    setSelectedIndex(undefined);
    toggle();
  };

  return (
    <>
      <Table<IQuotationDetail> rowKey="id" dataSource={watch('quotationDetails')} columns={columns} stickyHeader />
      <PriceListDeleteMaterialModals isOpen={isOpen} toggle={toggle} onOk={onOk} onCancel={onCancel} />
    </>
  );
};

export default PriceListFormTableMMS;

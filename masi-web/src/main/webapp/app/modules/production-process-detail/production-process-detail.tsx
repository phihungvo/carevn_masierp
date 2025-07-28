import './production-process-detail.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useEffect, useState } from 'react';
import { ProductionProcessDetailHeader, Template } from './production-process-detail-components';
import { useParams } from 'react-router';
import useProductionProcess from 'app/hooks/use-production-process';
import ModalConfirmDeleteTemplate from './modal-confirm-delete-template';
import { ProductionProcessDetailTable } from './production-process-detail-table';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { IProductionProcessDetailParams } from 'app/shared/model/production-process.model';
import { useDebounce } from 'app/hooks/use-debounce';

const { useGetProductionProcessById } = useProductionProcess;

const ProductionProcessDetail = () => {
  const { id } = useParams();

  const [isOpen, setIsOpen] = useState(false);
  const [selectedRecord, setSelectedRecord] = useState<{ template: Template; id: string }>();
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IProductionProcessDetailParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    startDate: '',
    endDate: '',
  });

  const toggle = () => setIsOpen(!isOpen);

  const searchString = useDebounce(searchText, 500);
  const { data } = useGetProductionProcessById(id, filter);

  useEffect(() => {
    setFilter(prev => ({ ...prev, name: searchString, page: DEFAULT_PAGE }));
  }, [searchString]);

  return (
    <>
      <Typography level={3}>Quản lý công đoạn sản xuất</Typography>
      <Card header={<ProductionProcessDetailHeader workItem={data?.workItem} setFilter={setFilter} setSearchText={setSearchText} />}>
        <ProductionProcessDetailTable
          status={data?.status}
          data={data?.workItem}
          setSelectedRecord={setSelectedRecord}
          toggle={toggle}
          filter={filter}
          setFilter={setFilter}
        />
      </Card>

      <ModalConfirmDeleteTemplate isOpen={isOpen} toggle={toggle} selectedRecord={selectedRecord} />
    </>
  );
};

export default ProductionProcessDetail;

import './uniform-exports.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsUniformExports } from 'app/hooks/use-modals-uniform';
import UniformExportsHeader from './uniform-exports-header';
import UniformExportsTable from './uniform-exports-table';
import UniformExportsFilterModals from './modals/uniform-exports-filter-modals';
import { IUniformReleaseParams } from 'app/shared/model/uniform.model';
import UniformExportsDetailsModals from './modals/uniform-exports-details-modals';
import UniformCreateModals from '../uniform/modals/uniform-create-modals';
import UniformCreateSuccessModals from '../uniform/modals/uniform-create-success-modals';

const UniformExports = () => {
  const [
    { openFilter, toggleFilter },
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openDetail, toggleDetail },
  ] = useModalsUniformExports();

  const [selectedRecord, setSelectedRecord] = useState<string>('');
  const [searchText, setSearchText] = useState<string>('');
  const [filter, setFilter] = useState<IUniformReleaseParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    search: '',
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Quản lý kho</Typography>

      <Card header={<UniformExportsHeader setSearchText={setSearchText} toggleFilter={toggleFilter} toggleCreate={toggleCreate} />}>
        <UniformExportsTable
          searchText={searchText}
          filter={filter}
          setFilter={setFilter}
          setSelectedRecord={setSelectedRecord}
          toggleDetail={toggleDetail}
        />
      </Card>

      <UniformExportsFilterModals isOpen={openFilter} toggle={toggleFilter} setFilter={setFilter} />
      {/* <UniformExportsCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UniformExportsCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} /> */}
      <UniformCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UniformCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
      <UniformExportsDetailsModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
    </div>
  );
};

export default UniformExports;

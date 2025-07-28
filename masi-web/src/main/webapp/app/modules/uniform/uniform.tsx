import './uniform.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import UniformHeader from './uniform-header';
import UniformTable from './uniform-table';
import UniformCreateModals from './modals/uniform-create-modals';
import UniformCreateSuccessModals from './modals/uniform-create-success-modals';
import { useModalsUniform } from 'app/hooks/use-modals-uniform';
import { IUniformStockParams } from 'app/shared/model/uniform.model';

const Uniform = () => {
  const [{ openCreate, toggleCreate }, { openCreateSuccess, toggleCreateSuccess }] = useModalsUniform();

  const [filter, setFilter] = useState<IUniformStockParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách đồng phục</Typography>

      <Card header={<UniformHeader toggleCreate={toggleCreate} />}>
        <UniformTable filter={filter} setFilter={setFilter} />
      </Card>

      <UniformCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleCreateSuccess} />
      <UniformCreateSuccessModals isOpen={openCreateSuccess} toggle={toggleCreateSuccess} />
    </div>
  );
};

export default Uniform;

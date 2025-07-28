import { Typography } from 'app/components/typography/typography';
import './annual-leave.scss';
import React from 'react';
import Card from 'app/components/card/card';
import AnnualLeaveHeader from './annual-leave-header';
import AnnualLeaveTable from './annual-leave-table';
import { useModalsAnnualLeave } from 'app/hooks/use-modals-annual-leave';
import AnnualLeaveCreateModals from './modals/annual-leave-create-modals';
import AnnualLeaveUpdateSuccessModals from './modals/annual-leave-update-success-modals';

const AnnualLeave = () => {
  const [{ openCreate, toggleCreate }, { openSuccess, toggleSuccess }] = useModalsAnnualLeave();

  return (
    <>
     <div className='page_container'>
      <Typography level={4}>Phép năm</Typography>

      <Card header={<AnnualLeaveHeader toggleCreate={toggleCreate} />}>
        <AnnualLeaveTable />
      </Card>

      <AnnualLeaveCreateModals isOpen={openCreate} toggle={toggleCreate} toggleSuccess={toggleSuccess} />
      <AnnualLeaveUpdateSuccessModals isOpen={openSuccess} toggle={toggleSuccess} />
      </div>
    </>
  );
};

export default AnnualLeave;

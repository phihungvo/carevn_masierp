import React from 'react';
import { Button } from 'reactstrap';

import { DateObject } from 'react-multi-date-picker';

interface IReportUniformsExpiredHeader {
  selectedDate: DateObject[];
  setSelectedDate: (value: DateObject[]) => void;
  toggleModalFilter: () => void;
}

const ReportUniformsExpiredHeader = (props: IReportUniformsExpiredHeader) => {
  const { toggleModalFilter } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra" />
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleModalFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg"/>
        </Button>
      </div>
    </div>
  );
};

export default ReportUniformsExpiredHeader;

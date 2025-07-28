import InputSearch from 'app/components/input/input-search';
import React from 'react';
import { Button } from 'reactstrap';

interface IRecruitmentHeaderProps {
  toggleFilter: () => void;
  setSearchText: (value: string) => void;
}

const RecruitmentHeaderCandidates = (props: IRecruitmentHeaderProps) => {
  const { toggleFilter, setSearchText } = props;

  return (
    <div className="card-header-container">
      <div className="card-header-extra">
        <InputSearch
          className="card-header-extra"
          onChange={e => {
            setSearchText(e.target.value);
          }}
        />
      </div>
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
      </div>
    </div>
  );
};

export default RecruitmentHeaderCandidates;

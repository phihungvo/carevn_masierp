import InputSearch from 'app/components/input/input-search';
import React from 'react';

interface IAuthoritiesHeaderProps {
  setSearchText: (value: string) => void;
}

const AuthoritiesHeader = (props: IAuthoritiesHeaderProps) => {
  const { setSearchText } = props;

  return (
    <div className="card-header-container">
      <InputSearch
        className="card-header-extra"
        onChange={e => {
          setSearchText(e.target.value);
        }}
      />

      <div className="card-header-extra"></div>
    </div>
  );
};

export default AuthoritiesHeader;

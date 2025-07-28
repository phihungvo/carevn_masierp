import React from 'react';

import InputSearch from 'app/components/input/input-search';

interface IAuthoritiesUsersHeaderProps {
  setSearchText: (value: string) => void;
}

const AuthoritiesUsersHeader = (props: IAuthoritiesUsersHeaderProps) => {
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

export default AuthoritiesUsersHeader;

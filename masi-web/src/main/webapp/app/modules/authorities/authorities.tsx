import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import AuthoritiesHeader from './authorities-header';
import AuthoritiesTable from './authorities-table';

const Authorities = () => {
  const [searchText, setSearchText] = useState<string>('');

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách quyền</Typography>

      <Card header={<AuthoritiesHeader setSearchText={setSearchText} />}>
        <AuthoritiesTable searchText={searchText} />
      </Card>
    </div>
  );
};

export default Authorities;

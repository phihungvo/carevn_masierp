import React from 'react';

import useGroup from 'app/hooks/use-group';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import Descriptions from '@uiw/react-descriptions';
import AccountTable from './components/account-table';
import AuthoritiesTable from './components/authorities-table';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { filterDataEmployee } from 'app/modules/authorities-group/utils/filterDataEmployee';

const { useGroupById } = useGroup;
const { useGetEmployeeProfilesQuery } = useEmployee;

interface IAuthoritiesGroupDetail {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const AuthoritiesGroupDetail = ({ isOpen, toggle, selectedRecord }: IAuthoritiesGroupDetail) => {

  const { data } = useGroupById(selectedRecord);
  const { data: listEmployee } = useGetEmployeeProfilesQuery({ page: DEFAULT_PAGE, size: DEFAULT_PAGE_SIZE_NAX });

  const employees = filterDataEmployee(listEmployee?.data, data?.users?.map(item => item?.id));

  return (
    < Modal
      isOpen={isOpen}
      toggle={toggle}
      ok={false}
      cancel={false}
      fullscreen
      className='modal-default'
      titleHeader='Chi tiết nhóm quyền'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Tên nhóm quyền">
            <Typography level="text" className="fw-bolder">
              {data?.name}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Mô tả">
            <Typography level="text" className="fw-bolder">
              {data?.description}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Danh sách các quyền" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chi tiết">
            <AuthoritiesTable data={data?.authorities} />
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Danh sách người dùng" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chi tiết">
            <AccountTable data={employees} />
          </Descriptions.Item>
        </Descriptions>
      </Flex>
    </ Modal>
  );
};

export default AuthoritiesGroupDetail;

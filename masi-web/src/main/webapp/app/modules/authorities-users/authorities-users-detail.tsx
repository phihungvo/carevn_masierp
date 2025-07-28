import React from 'react';

import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import useEmployee from 'app/hooks/use-employee';
import Descriptions from '@uiw/react-descriptions';
import useAdminUsers from 'app/hooks/use-admin-users';
import recruitmentMapping from '../recruitment/recruitment-mapping';
import AuthoritiesTable from '../authorities-group/components/authorities-table';
import { Typography } from 'app/components/typography/typography';
import { RECRUITMENT_POSITION } from 'app/shared/model/enumerations/recruitment.model';

const { useGetAdminUserGroups } = useAdminUsers;
const { useGetEmployeeProfileByIdQuery } = useEmployee;
const { recruitmentPositionTextMapping } = recruitmentMapping;

interface IAuthoritiesUsersDetail {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
}

const AuthoritiesUsersDetail = ({ isOpen, toggle, selectedRecord }: IAuthoritiesUsersDetail) => {

  const { data: employee } = useGetEmployeeProfileByIdQuery(selectedRecord);
  const { data } = useGetAdminUserGroups(selectedRecord)

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      ok={false}
      cancel={false}
      className="authorities-user-detail-modals"
      titleHeader='Chi tiết người dùng'
    >
      <Flex direction="column" gap={16}>
        <Descriptions title="Thông tin chung" size="large" bordered column={2}>
          <Descriptions.Item label="Mã nhân viên">
            <Typography level="text" className="fw-bolder">
              {employee?.employeeCode}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Tên người dùng">
            <Typography level="text" className="fw-bolder">
              {employee?.fullName}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Phòng ban">
            <Typography level="text" className="fw-bolder">
              {employee?.workspace?.name}
            </Typography>
          </Descriptions.Item>

          <Descriptions.Item label="Chức vụ">
            <Typography level="text" className="fw-bolder">
              {recruitmentPositionTextMapping(employee?.position as RECRUITMENT_POSITION)}
            </Typography>
          </Descriptions.Item>
        </Descriptions>

        <div className="divider" />

        <Descriptions title="Danh sách nhóm quyền" size="large" bordered column={1} layout="vertical">
          <Descriptions.Item label="Chi tiết">
            <AuthoritiesTable data={data} />
          </Descriptions.Item>
        </Descriptions>

      </Flex>
    </Modal>
  );
};

export default AuthoritiesUsersDetail;

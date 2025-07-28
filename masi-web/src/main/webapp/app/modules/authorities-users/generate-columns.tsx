import React, { useMemo } from 'react';

import ActionsDropdown from './components/actions-dropdown';
import recruitmentMapping from 'app/modules/recruitment/recruitment-mapping';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { isHasPermission } from 'app/constants/common';
import { useAppSelector } from 'app/config/store';

const { recruitmentPositionTextMapping } = recruitmentMapping;

export const generateColumns = (
  setSelectedRecord: (id: string) => void,
  toggleUpdateUsers: () => void,
  toggleDetail: () => void,
  selectedRow: any,
  setSelectedRow: React.Dispatch<any>,
  toggleUpdateAccount: () => void,
  toggleSettingCompanies: () => void
): ColumnsTypes<IEmployeeProfiles> => {
  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: ColumnsTypes<IEmployeeProfiles> = useMemo(() => {

    const handleDetail = (id: string) => {
      setSelectedRecord(id);
      toggleDetail();
    };

    return [
      {
        title: 'Mã nhân viên',
        key: 'employeeCode',
        dataIndex: 'employeeCode',
        render: (text, record) => (
          <Tooltip label={text} target={`employeeCode-${record.id}`}>
            <p className="attachment-link" onClick={() => {
              if (!isHasPermission(authorities, 'PERMISSIONS_USERS.VIEW')) return
              handleDetail(record?.id)
            }}>
              <EllipsisParagraph text={text} width={100} id={`employeeCode-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Tên nhân viên',
        key: 'fullName',
        dataIndex: 'fullName',
        render: (text, record) => (
          <Tooltip label={text} target={`fullName-${record.id}`}>
            <EllipsisParagraph text={text} id={`fullName-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Phòng ban',
        key: 'work-space',
        render: (text, record) => (
          <Tooltip label={record?.workspace?.name} target={`work-space-${record.id}`}>
            <EllipsisParagraph text={record?.workspace?.name} id={`work-space-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Chức vụ',
        dataIndex: 'position',
        key: 'position',
        render: (text, record) => (
          <Tooltip label={recruitmentPositionTextMapping(text)} target={`position-${record.id}`}>
            <EllipsisParagraph text={recruitmentPositionTextMapping(text)} id={`position-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            record={record}
            setSelectedRecord={setSelectedRecord}
            toggleUpdateUsers={toggleUpdateUsers}
            toggleDetail={toggleDetail}
            selectedRow={selectedRow}
            setSelectedRow={setSelectedRow}
            toggleUpdateAccount={toggleUpdateAccount}
            toggleSettingCompanies={toggleSettingCompanies}
          />
        ),
      },
    ];
  }, []);

  return columns;
};

import { default as React, useMemo } from 'react';

import Badge from 'app/components/badge/badge';
import Input from 'app/components/input/input';
import employeesMapping from './employees-mapping';
import Tooltip from 'app/components/tooltip/tooltip';
import ActionsDropdown from './components/actions-dropdown';
import recruitmentMapping from '../recruitment/recruitment-mapping';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { UserCheckIcon } from './components/user-icons';
import { ColumnsTypes } from 'app/components/table/table.d';
import { IEmployeeParams, IEmployeeProfiles } from 'app/shared/model/employee.model';
import recruimentMapping from 'app/modules/recruitment/recruitment-mapping'
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';

const { employeeProfileStatusMapping, employeeProfileStatusColorMapping, genderTextMapping } = employeesMapping;
const { recruitmentPositionTextMapping } = recruitmentMapping;
const { recruitmentContractTypeTextMapping } = recruimentMapping

export const generateColumns = (
  toggleDetail: () => void,
  toggleDeactivate: () => void,
  toggleActivate: () => void,
  toggleUpload: () => void,
  toggleConfirmLeave: () => void,
  setSelectedRecord: (id: string) => void,
  filter: IEmployeeParams,
  toggleProvideAccount: () => void,
  toggleActivateTimkeepingDevicer: () => void,
): ColumnsTypes<IEmployeeProfiles> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IEmployeeProfiles> = useMemo(() => {
    return [
      {
        title: 'Mã NV',
        dataIndex: 'employeeCode',
        key: 'employeeCode',
        render: (text, record) => (
          <>
            <p className="attachment-link" onClick={() => handleDetail(record.id)}>
              {text}
            </p>
            {
              record.account && (<span style={{ display: 'flex', alignItems: 'center' }}><span>&nbsp;&nbsp;-&nbsp;</span>
                <UserCheckIcon fill='green' /></span>
              )
            }
          </>
        ),

      },
      {
        title: 'Tên NV',
        dataIndex: 'fullName',
        key: 'fullName',
        render: (text, record) => (
          <Tooltip label={text} target={`fullName-${record.id}`}>
            <EllipsisParagraph text={text} width={140} id={`fullName-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Phòng ban',
        dataIndex: 'workspaceId',
        key: 'workspaceId',
        render: (text, record) => (
          <Tooltip label={record.workspace?.name} target={`workspaceId-${record.id}`}>
            <EllipsisParagraph text={record.workspace?.name} width={140} id={`workspaceId-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Chức vụ',
        dataIndex: 'position',
        key: 'position',
        render: (text, record) => (
          <Tooltip label={recruitmentPositionTextMapping(text)} target={`position-${record.id}`}>
            <EllipsisParagraph text={recruitmentPositionTextMapping(text)} width={140} id={`position-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Ngày vào làm',
        dataIndex: 'startWorkDate',
        key: 'startWorkDate',
        width: 110,
        render: text => <p>{dayjs(text).format(DATE_FORMAT.DATE)}</p>,
      },
      {
        title: 'Loại hợp đồng',
        dataIndex: 'contractType',
        key: 'contractType',
        width: 110,
        render: (text, record) => (
          <Tooltip label={recruitmentContractTypeTextMapping(text)} target={`contractType-${record.id}`}>
            <EllipsisParagraph text={recruitmentContractTypeTextMapping(text)} width={140} id={`contractType-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Phép năm',
        key: 'annualLeave',
        width: 120,
        render: (_, record) => (
          `${record?.useDaysOff ?? 0}/${record?.numberDaysOff ?? 0}`
        )
      },
      {
        title: 'Trạng thái',
        dataIndex: 'status',
        key: 'status',
        render: text => <Badge color={employeeProfileStatusColorMapping(text)}>{employeeProfileStatusMapping(text)}</Badge>,
      },
      // {
      //   title: 'Trạng thái',
      //   dataIndex: 'isActive',
      //   key: 'isActive',
      //   render: isActive => <Badge color={isActive ? 'primary' : 'error'}>{activeTextMapping(isActive)}</Badge>,
      // },
      {
        title: 'Đã tải hồ sơ',
        key: 'uploaded-profile',
        align: 'center',
        width: 130,
        render: (_, record) => <Input type='checkbox' checked={record?.isHasProfileAttachment} />
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleDetail={toggleDetail}
            toggleDeactivate={toggleDeactivate}
            toggleActivate={toggleActivate}
            toggleUpload={toggleUpload}
            toggleConfirmLeave={toggleConfirmLeave}
            record={record}
            setSelectedRecord={setSelectedRecord}
            filter={filter}
            toggleProvideAccount={toggleProvideAccount}
            toggleActivateTimkeepingDevicer={toggleActivateTimkeepingDevicer}
          />
        ),
      },
    ];
  }, [filter]);

  return columns;
};

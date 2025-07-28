import { ColumnsTypes } from 'app/components/table/table.d';
import recruitmentMapping from 'app/modules/recruitment/recruitment-mapping';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';

const { recruitmentPositionTextMapping } = recruitmentMapping;

export const generateColumnsAccount = (): ColumnsTypes<IEmployeeProfiles> => {
  const columns: ColumnsTypes<IEmployeeProfiles> = [
    {
      title: 'Mã NV',
      dataIndex: 'employeeCode',
      key: 'employeeCode',
    },
    {
      title: 'Tên NV',
      dataIndex: 'fullName',
      key: 'fullName',
    },
    {
      title: 'Phòng ban',
      dataIndex: 'workspaceId',
      key: 'workspaceId',
      render: (_, record) => record.workspace?.name,
    },
    {
      title: 'Chức vụ',
      dataIndex: 'position',
      key: 'position',
      render: text => recruitmentPositionTextMapping(text),
    },
  ];

  return columns;
};

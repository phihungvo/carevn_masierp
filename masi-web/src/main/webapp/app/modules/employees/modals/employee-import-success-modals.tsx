import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import React from 'react';

interface IModalsImportSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeImportSuccessModals = (props: IModalsImportSuccess) => {
  const { isOpen, toggle } = props;

  const importResponse: IEmployeeProfiles[] = useAppSelector(state => state.employees.importResponse);

  const isSuccess = importResponse?.every(item => !item?.ignore);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      titleHeader={`Tải lên danh sách nhân viên ${isSuccess ? 'thành công' : 'thất bại'}`}
    >
      {isSuccess ? (
        <Typography level={4}>Bạn đã tải lên danh sách nhân viên thành công</Typography>
      ) : (
        <Flex direction="column" gap={4}>
          {importResponse
            ?.filter(item => item.ignore)
            ?.map((item, index) => (
              <Typography key={index} level="text">
                {<b>- {item?.fullName}</b> || ''}: <div dangerouslySetInnerHTML={{ __html: item.error }} />
              </Typography>
            ))}
        </Flex>
      )}
    </Modal>
  );
};

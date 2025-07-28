import React from 'react';

import Flex from "app/components/flex/flex";
import Modal from 'app/components/modal/modal';
import useEmployee from "app/hooks/use-employee";
import ReportUniformsExpiredHistoryTable from "app/modules/report-uniforms-expired/report-uniforms-expired-history-table";
import { Row } from "reactstrap";
import { Typography } from 'app/components/typography/typography';

const { useGetEmployeeProfileByIdQuery } = useEmployee;

interface IReportUniformsExpiredHistoryModalProps {
  isOpen: boolean;
  toggle: () => void;
  employeeIds: string
}

const ReportUniformsExpiredHistoryModal = (props: IReportUniformsExpiredHistoryModalProps) => {
  const { isOpen, toggle, employeeIds } = props;
  const { data } = useGetEmployeeProfileByIdQuery(employeeIds);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancelText="Đặt lại"
      okText="Áp dụng"
      className="report-uniform-expired-history-modal"
      ok={false}
      cancel={false}
      titleHeader='Lịch sử'
    >
      <Flex direction='column' gap={16}>
        <Row>
          <Flex gap={16}>
            <Typography level="paragraph" className="bold">
              Tên nhân viên:
            </Typography>
            <Typography level="paragraph">{data?.fullName}</Typography>
          </Flex>
        </Row>
        <ReportUniformsExpiredHistoryTable employeeIds={employeeIds} />
      </Flex>
    </Modal>
  );
};

export default ReportUniformsExpiredHistoryModal;

import React from 'react';
import dayjs from "dayjs";
import { Col, Row } from "reactstrap";

import Flex from "app/components/flex/flex";
import Modal from 'app/components/modal/modal';
import useRecruitment from "app/hooks/use-recruitment";
import { DATE_FORMAT } from "app/constants/common";
import { Typography } from 'app/components/typography/typography';

interface IRecruitmentHistoryModalsProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string
}

const { useGetRecruitmentHistory } = useRecruitment;

const RecruitmentHistoryModals = (props: IRecruitmentHistoryModalsProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { data } = useGetRecruitmentHistory(selectedRecord);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-recruitment-history"
      ok={false}
      cancel={false}
      titleHeader='Lịch sử gia hạn'
    >
      {
        data?.data?.map((record, index) => {
          return (
            <Flex direction="column" gap={40}>
              <Row>{record?.change?.map((item) => <React.Fragment key={item?.fieldName}>
                {
                  item?.fieldName === 'deadline' && <Col md={12} className="mb-4">
                    <Flex gap={16}>
                      <Typography level="paragraph" className="bold">
                        {`Gia hạn lần ${index + 1}`}:
                      </Typography>
                      <Typography level="paragraph">{item?.oldValue && dayjs(item?.oldValue).format(DATE_FORMAT.DATE)}</Typography> {'->'}
                      <Typography level="paragraph">{item?.newValue && dayjs(item?.newValue).format(DATE_FORMAT.DATE)}</Typography>
                    </Flex>
                  </Col>
                }

              </React.Fragment>)}</Row>
            </Flex>
          )
        })
      }
    </Modal>
  );
};

export default RecruitmentHistoryModals;
